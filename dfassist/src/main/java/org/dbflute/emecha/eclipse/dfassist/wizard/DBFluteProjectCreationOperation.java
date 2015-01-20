/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.dbflute.emecha.eclipse.dfassist.wizard;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.dbflute.emecha.eclipse.dfassist.nls.Messages;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.ide.undo.CreateProjectOperation;
import org.eclipse.ui.ide.undo.WorkspaceUndoUtil;

/**
 * プロジェクト作成操作
 */
public class DBFluteProjectCreationOperation implements IRunnableWithProgress {

    private Shell shell;
    private IWorkbench workbench;
    private IWorkingSet[] workingSets;
    private IProject project;
    private IPath location;
    private String packaging;
    private String artifactName;
    private String description;
    private ArtifactInfo artifactInfo;
    private ArtifactInfo parentArtifactInfo;
    private String dbfluteVersion;
    private String clientName;
    private String packageBase;
    private String javaVersion = "1.8";

    private static class ArtifactInfo {
        public String groupId;
        public String artifactId;
        public String version;
    }

    public DBFluteProjectCreationOperation(Shell shell) {
        this.shell = shell;
    }

    /**
     * {@inheritDoc}
     * @see org.eclipse.jface.operation.IRunnableWithProgress#run(org.eclipse.core.runtime.IProgressMonitor)
     */
    @Override
    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        monitor.beginTask(Messages.NewProjectWizard_ProgressTitle, 9);
        try {
            // プロジェクト作成
            if (monitor.isCanceled())
                throw new InterruptedException(Messages.NewProjectWizardTask_cancel);
            IProject newProject = createProject(project, location, monitor);
            monitor.worked(1);
            // ワーキングセットへ追加
            if (monitor.isCanceled())
                throw new InterruptedException(Messages.NewProjectWizardTask_cancel);
            appendWorkingSet(project);
            monitor.worked(1);
            // ネイチャー追加
            if (monitor.isCanceled())
                throw new InterruptedException(Messages.NewProjectWizardTask_cancel);
            addNature(newProject, monitor);
            monitor.worked(1);
            // ビルドコマンド追加
            if (monitor.isCanceled())
                throw new InterruptedException(Messages.NewProjectWizardTask_cancel);
            addBuildCommand(newProject, monitor);
            monitor.worked(1);
            // ソースフォルダ作成
            if (monitor.isCanceled())
                throw new InterruptedException(Messages.NewProjectWizardTask_cancel);
            createFolders(newProject, monitor);
            monitor.worked(1);
            // クラスパス追加
            if (monitor.isCanceled())
                throw new InterruptedException(Messages.NewProjectWizardTask_cancel);
            createClasspathFile(newProject, monitor);
            monitor.worked(1);
            // POMファイル作成
            if (monitor.isCanceled())
                throw new InterruptedException(Messages.NewProjectWizardTask_cancel);
            createPomFile(newProject, monitor);
            monitor.worked(1);
            // プロジェクト設定追加
            if (monitor.isCanceled())
                throw new InterruptedException(Messages.NewProjectWizardTask_cancel);
            createProjectSettings(newProject, monitor);
            monitor.worked(1);
            // ワークスペースをリフレッシュ
            ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, monitor);
            monitor.worked(1);
        } catch (CoreException | IOException e) {
            throw new InvocationTargetException(e);
        }
        monitor.done();
    }

    private IProject createProject(IProject newProject, IPath location, IProgressMonitor monitor) throws InvocationTargetException {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IProjectDescription description = workspace.newProjectDescription(newProject.getName());
        if (location != null) {
            description.setLocationURI(location.toFile().toURI());
        }
        CreateProjectOperation op = new CreateProjectOperation(description, Messages.NewProjectWizardTitle);
        try {
            op.execute(monitor, WorkspaceUndoUtil.getUIInfoAdapter(shell));
        } catch (ExecutionException e) {
            throw new InvocationTargetException(e);
        }
        return workspace.getRoot().getProject(newProject.getName());
    }

    private void appendWorkingSet(IProject newProject) {
        this.workbench.getWorkingSetManager().addToWorkingSets(newProject, workingSets);
    }

    private void addNature(IProject project, IProgressMonitor monitor) throws CoreException {
        IProjectDescription description = project.getDescription();
        description.setNatureIds(new String[] { "org.eclipse.jdt.core.javanature", "org.eclipse.m2e.core.maven2Nature" });
        project.setDescription(description, monitor);
    }

    private void addBuildCommand(IProject project, IProgressMonitor monitor) throws CoreException {
        IProjectDescription description = project.getDescription();

        ICommand javabuilderCommand = description.newCommand();
        javabuilderCommand.setBuilderName("org.eclipse.jdt.core.javabuilder");

        ICommand maven2BuilderCommand = description.newCommand();
        maven2BuilderCommand.setBuilderName("org.eclipse.m2e.core.maven2Builder");

        description.setBuildSpec(new ICommand[] { javabuilderCommand, maven2BuilderCommand });

        project.setDescription(description, monitor);
    }

    private void createFolders(IProject project, IProgressMonitor monitor) throws CoreException {
        SubProgressMonitor subMonitor = new SubProgressMonitor(monitor, 7);
        IFolder srcFolder = project.getFolder("src");
        srcFolder.create(true, true, subMonitor);

        IFolder mainFolder = srcFolder.getFolder("main");
        mainFolder.create(true, true, subMonitor);

        IFolder mainJavaFolder = mainFolder.getFolder("java");
        mainJavaFolder.create(true, true, subMonitor);

        IFolder mainResourceFolder = mainFolder.getFolder("resources");
        mainResourceFolder.create(true, true, subMonitor);

        if (isWarProject()) {
            IFolder mainWebappFolder = mainFolder.getFolder("webapp");
            mainWebappFolder.create(true, true, subMonitor);
        }

        IFolder testFolder = srcFolder.getFolder("test");
        testFolder.create(true, true, subMonitor);

        IFolder testJavaFolder = testFolder.getFolder("java");
        testJavaFolder.create(true, true, subMonitor);

        IFolder testResourceFolder = testFolder.getFolder("resources");
        testResourceFolder.create(true, true, subMonitor);

    }

    private static final String separator = System.getProperty("line.separator", "\n");

    private void createClasspathFile(IProject project, IProgressMonitor monitor) throws CoreException, IOException {
        StringBuilder str = new StringBuilder();
        str.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(separator);
        str.append("<classpath>").append(separator);
        str.append("\t<classpathentry kind=\"src\" output=\"target/classes\" path=\"src/main/java\"/>").append(separator);
        str.append("\t<classpathentry kind=\"src\" excluding=\"**\" output=\"target/classes\" path=\"src/main/resources\"/>").append(
                separator);
        str.append("\t<classpathentry kind=\"src\" output=\"target/test-classes\" path=\"src/test/java\"/>").append(separator);
        str.append("\t<classpathentry kind=\"src\" excluding=\"**\" output=\"target/test-classes\" path=\"src/test/resources\"/>").append(
                separator);
        str.append(
                "\t<classpathentry kind=\"con\" path=\"org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-")
                .append(javaVersion).append("\"/>").append(separator);
        str.append("\t<classpathentry kind=\"con\" path=\"org.eclipse.m2e.MAVEN2_CLASSPATH_CONTAINER\"/>").append(separator);
        str.append("\t<classpathentry kind=\"output\" path=\"target/classes\"/>").append(separator);
        str.append("</classpath>").append(separator);

        IFile classpathFile = project.getFile(".classpath");
        InputStream source = new ByteArrayInputStream(str.toString().getBytes("UTF-8"));
        classpathFile.create(source, false, new SubProgressMonitor(monitor, 1));
    }

    private void createPomFile(IProject project, IProgressMonitor monitor) throws IOException, CoreException {
        StringBuilder str = new StringBuilder();
        str.append("<project xmlns=\"http://maven.apache.org/POM/4.0.0\"").append(separator);
        str.append("         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"").append(separator);
        str.append("         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">")
                .append(separator);
        str.append(separator);

        if (hasParent()) {
            str.append("  <parent>").append(separator);
            str.append("    <groupId>").append(this.parentArtifactInfo.groupId).append("</groupId>").append(separator);
            str.append("    <artifactId>").append(this.parentArtifactInfo.artifactId).append("</artifactId>").append(separator);
            str.append("    <version>").append(this.parentArtifactInfo.version).append("</version>").append(separator);
            str.append("  </parent>").append(separator);

            str.append(separator);
        }

        str.append("  <modelVersion>4.0.0</modelVersion>").append(separator);
        str.append(separator);
        str.append("  <groupId>").append(this.artifactInfo.groupId).append("</groupId>").append(separator);
        str.append("  <artifactId>").append(this.artifactInfo.artifactId).append("</artifactId>").append(separator);
        str.append("  <version>").append(this.artifactInfo.version).append("</version>").append(separator);
        if (isWarProject()) {
            str.append("  <packaging>war</packaging>").append(separator);
        }
        str.append("  <name>").append(this.artifactName).append("</name>").append(separator);
        str.append("  <description>").append(this.description).append("</description>").append(separator);
        str.append(separator);
        str.append("  <properties>").append(separator);
        str.append("    <dbflute.version>").append(this.dbfluteVersion).append("</dbflute.version>").append(separator);
        str.append("  </properties>").append(separator);
        if (isLambdaVersion()) {
            str.append(separator);
            str.append("  <dependencies>").append(separator);
            str.append("    <dependency>").append(separator);
            str.append("      <groupId>org.dbflute</groupId>").append(separator);
            str.append("      <artifactId>dbflute-runtime</artifactId>").append(separator);
            str.append("      <version>${dbflute.version}</version>").append(separator);
            str.append("    </dependency>").append(separator);
            str.append("  </dependencies>").append(separator);
        } else {
            str.append(separator);
            str.append("  <dependencies>").append(separator);
            str.append("    <dependency>").append(separator);
            str.append("      <groupId>org.seasar.dbflute</groupId>").append(separator);
            str.append("      <artifactId>dbflute-runtime</artifactId>").append(separator);
            str.append("      <version>${dbflute.version}</version>").append(separator);
            str.append("    </dependency>").append(separator);
            str.append("  </dependencies>").append(separator);
            str.append(separator);
            str.append("  <pluginRepositories>").append(separator);
            str.append("    <pluginRepository>").append(separator);
            str.append("      <id>maven.seasar.org</id>").append(separator);
            str.append("      <name>The Seasar Foundation Maven2 Repository</name>").append(separator);
            str.append("      <url>http://maven.seasar.org/maven2</url>").append(separator);
            str.append("    </pluginRepository>").append(separator);
            //            str.append("    <pluginRepository>").append(separator);
            //            str.append("      <id>maven-snapshot.seasar.org</id>").append(separator);
            //            str.append("      <name>The Seasar Foundation Maven2 Repository</name>").append(separator);
            //            str.append("      <url>http://maven.seasar.org/maven2-snapshot</url>").append(separator);
            //            str.append("    </pluginRepository>").append(separator);
            str.append("  </pluginRepositories>").append(separator);
            str.append("  <repositories>").append(separator);
            str.append("    <repository>").append(separator);
            str.append("      <id>maven.seasar.org</id>").append(separator);
            str.append("      <name>The Seasar Foundation Maven2 Repository</name>").append(separator);
            str.append("      <url>http://maven.seasar.org/maven2</url>").append(separator);
            str.append("      <snapshots>").append(separator);
            str.append("        <enabled>false</enabled>").append(separator);
            str.append("      </snapshots>").append(separator);
            str.append("    </repository>").append(separator);
            //            str.append("    <repository>").append(separator);
            //            str.append("      <id>maven-snapshot.seasar.org</id>").append(separator);
            //            str.append("      <name>The Seasar Foundation Maven2 Repository</name>").append(separator);
            //            str.append("      <url>http://maven.seasar.org/maven2-snapshot</url>").append(separator);
            //            str.append("      <releases>").append(separator);
            //            str.append("        <enabled>false</enabled>").append(separator);
            //            str.append("      </releases>").append(separator);
            //            str.append("      <snapshots>").append(separator);
            //            str.append("        <enabled>true</enabled>").append(separator);
            //            str.append("        <updatePolicy>always</updatePolicy>").append(separator);
            //            str.append("      </snapshots>").append(separator);
            //            str.append("    </repository>").append(separator);
            str.append("  </repositories>").append(separator);
        }
        str.append(separator);
        str.append("  <build>").append(separator);
        str.append("    <plugins>").append(separator);
        str.append("      <plugin>").append(separator);
        str.append("        <artifactId>maven-compiler-plugin</artifactId>").append(separator);
        str.append("        <configuration>").append(separator);
        str.append("          <source>").append(this.javaVersion).append("</source>").append(separator);
        str.append("          <target>").append(this.javaVersion).append("</target>").append(separator);
        str.append("          <encoding>UTF-8</encoding>").append(separator);
        str.append("        </configuration>").append(separator);
        str.append("      </plugin>").append(separator);
        if (isLambdaVersion()) {
            str.append("      <plugin>").append(separator);
            str.append("        <groupId>org.dbflute</groupId>").append(separator);
            str.append("        <artifactId>dbflute-maven-plugin</artifactId>").append(separator);
            str.append("        <configuration>").append(separator);
            str.append("          <dbfluteVersion>${dbflute.version}</dbfluteVersion>").append(separator);
            str.append("          <clientProject>").append(this.clientName).append("</clientProject>").append(separator);
            str.append("          <packageBase>").append(this.packageBase).append("</packageBase>").append(separator);
            //            str.append("          <!--").append(separator);
            //            str.append("          <targetLanguage>java</targetLanguage>").append(separator);
            //            str.append("          <targetContainer>spring</targetContainer>").append(separator);
            //            str.append("          <database>h2</database>").append(separator);
            //            str.append("          <databaseDriver>org.h2.Driver</databaseDriver>").append(separator);
            //            str.append("          <databaseUrl>jdbc:h2:file:../src/main/resources/").append(this.clientName).append("</databaseUrl>").append(separator);
            //            str.append("          <databaseSchema> </databaseSchema>").append(separator);
            //            str.append("          <databaseUser>sa</databaseUser>").append(separator);
            //            str.append("          <databasePassword> </databasePassword>").append(separator);
            //            str.append("           -->").append(separator);
            str.append("        </configuration>").append(separator);
            str.append("      </plugin>").append(separator);
        } else {
            str.append("      <plugin>").append(separator);
            str.append("        <groupId>org.seasar.dbflute</groupId>").append(separator);
            str.append("        <artifactId>dbflute-maven-plugin</artifactId>").append(separator);
            str.append("        <configuration>").append(separator);
            str.append("          <dbfluteVersion>${dbflute.version}</dbfluteVersion>").append(separator);
            str.append("          <clientProject>").append(this.clientName).append("</clientProject>").append(separator);
            str.append("          <dbPackage>").append(this.packageBase).append("</dbPackage>").append(separator);
            //            str.append("          <!--").append(separator);
            //            str.append("          <targetLanguage>java</targetLanguage>").append(separator);
            //            str.append("          <targetContainer>seasar</targetContainer>").append(separator);
            //            str.append("          <database>h2</database>").append(separator);
            //            str.append("          <databaseDriver>org.h2.Driver</databaseDriver>").append(separator);
            //            str.append("          <databaseUrl>jdbc:h2:file:../src/main/resources/").append(this.clientName).append("</databaseUrl>").append(separator);
            //            str.append("          <databaseSchema> </databaseSchema>").append(separator);
            //            str.append("          <databaseUser>sa</databaseUser>").append(separator);
            //            str.append("          <databasePassword> </databasePassword>").append(separator);
            //            str.append("           -->").append(separator);
            str.append("        </configuration>").append(separator);
            str.append("      </plugin>").append(separator);
        }
        str.append("    </plugins>").append(separator);
        str.append("  </build>").append(separator);
        str.append("</project>").append(separator);
        IFile pomFile = project.getFile("pom.xml");
        InputStream source = new ByteArrayInputStream(str.toString().getBytes("UTF-8"));
        pomFile.create(source, false, new SubProgressMonitor(monitor, 1));
    }

    private boolean isLambdaVersion() {
        return "1.8".equals(this.javaVersion);
    }

    private void createProjectSettings(IProject project, IProgressMonitor monitor) throws IOException, CoreException {
        SubProgressMonitor subMonitor = new SubProgressMonitor(monitor, 4);

        IFolder folder = project.getFolder(".settings");
        folder.create(true, true, subMonitor);
        subMonitor.worked(1);

        StringBuilder str = new StringBuilder();
        str.append("eclipse.preferences.version=1").append(separator);
        str.append("encoding/<project>=UTF-8").append(separator);
        IFile resourcesPrefs = folder.getFile("org.eclipse.core.resources.prefs");
        InputStream resourcesPrefsSource = new ByteArrayInputStream(str.toString().getBytes("UTF-8"));
        resourcesPrefs.create(resourcesPrefsSource, false, subMonitor);
        subMonitor.worked(1);

        str = new StringBuilder();
        str.append("eclipse.preferences.version=1").append(separator);
        str.append("org.eclipse.jdt.core.compiler.codegen.targetPlatform=").append(this.javaVersion).append(separator);
        str.append("org.eclipse.jdt.core.compiler.compliance=").append(this.javaVersion).append(separator);
        str.append("org.eclipse.jdt.core.compiler.problem.forbiddenReference=warning").append(separator);
        str.append("org.eclipse.jdt.core.compiler.source=").append(this.javaVersion).append(separator);
        IFile jdtPrefs = folder.getFile("org.eclipse.jdt.core.prefs");
        InputStream jdtPrefsSource = new ByteArrayInputStream(str.toString().getBytes("UTF-8"));
        jdtPrefs.create(jdtPrefsSource, false, subMonitor);
        subMonitor.worked(1);

        str = new StringBuilder();
        str.append("eclipse.preferences.version=1").append(separator);
        str.append("activeProfiles=").append(separator);
        str.append("resolveWorkspaceProjects=true").append(separator);
        str.append("version=1").append(separator);
        IFile m2ePrefs = folder.getFile("org.eclipse.m2e.core.prefs");
        InputStream m2ePrefsSource = new ByteArrayInputStream(str.toString().getBytes("UTF-8"));
        m2ePrefs.create(m2ePrefsSource, false, subMonitor);
        subMonitor.worked(1);

        subMonitor.done();
    }

    public void setWorkbench(IWorkbench workbench) {
        this.workbench = workbench;
    }

    public void setWorkingSets(IWorkingSet[] workingSets) {
        this.workingSets = workingSets;
    }

    public void setProject(IProject project) {
        this.project = project;
    }

    public void setLocation(IPath projectDir) {
        this.location = projectDir;
    }

    public void setPackaging(String packaging) {
        this.packaging = packaging;
    }

    protected boolean isWarProject() {
        return "war".equals(packaging);
    }

    public void setArtifactName(String artifactName) {
        this.artifactName = artifactName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setArtifactInfo(String groupId, String artifactId, String version) {
        ArtifactInfo info = new ArtifactInfo();
        info.groupId = groupId;
        info.artifactId = artifactId;
        info.version = version;
        this.artifactInfo = info;
    }

    public void setParentArtifactInfo(String groupId, String artifactId, String version) {
        ArtifactInfo info = new ArtifactInfo();
        info.groupId = groupId;
        info.artifactId = artifactId;
        info.version = version;
        this.parentArtifactInfo = info;
    }

    private boolean hasParent() {
        return this.parentArtifactInfo != null;
    }

    public void setDbfluteVersion(String dbfluteVersion) {
        this.dbfluteVersion = dbfluteVersion;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setPackageBase(String packageBase) {
        this.packageBase = packageBase;
    }

    public void setJreVersion(String jreVersion) {
        this.javaVersion = jreVersion;
    }

}
