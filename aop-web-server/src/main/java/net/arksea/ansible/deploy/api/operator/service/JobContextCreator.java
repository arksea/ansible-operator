package net.arksea.ansible.deploy.api.operator.service;


import net.arksea.ansible.deploy.api.ServiceException;
import net.arksea.ansible.deploy.api.manage.entity.AppOperation;
import net.arksea.ansible.deploy.api.manage.entity.Host;
import net.arksea.ansible.deploy.api.operator.entity.OperationJob;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Queue;
import java.util.Set;


/**
 *
 * @author xiaohaixing
 */
public class JobContextCreator {

    private JobResources resources;
    private OperationJob job;
    private Set<Long> hosts;
    private Queue<String> logsQueue;

    public JobContextCreator(OperationJob job, Set<Long> hosts, JobResources resources, Queue<String> logsQueue) {
        this.job = job;
        this.hosts = hosts;
        this.resources = resources;
        this.logsQueue = logsQueue;
    }

    public void run() {
        try {
            logsQueue.offer("初始化工作目录:\n");
            logsQueue.offer("生成playbook文件...");
            generatePlaybookFile();
            logsQueue.offer("成功\n生成hosts文件...");
            generateHostFile();
            logsQueue.offer("成功\n");
            logsQueue.offer("初始化工作目录完成\n");
        } catch (Exception ex) {
            logsQueue.offer("失败\n");
            throw new ServiceException("初始化工作目录失败", ex);
        }
    }

    private String getJobPath() {
        LocalDate localDate = LocalDate.now();
        return resources.jobWorkRoot + "/" + localDate + "/" + job.getId() + "/";
    }

    private void generatePlaybookFile() throws IOException {
        AppOperation op = resources.appOperationDao.findOne(job.getOperationId());
        final File file = new File(getJobPath() + "operation.yml" );
        final File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        try (final FileWriter writer = new FileWriter(file)) {
            writer.append(op.getPlaybook());
            writer.flush();
        }
    }

//    public void initContext(final String path) throws IOException {
//
//
//        final Set<Version> targetVersions = new HashSet<>();
//        for (final Host host : hosts) {
//            for (final Version v : app.getVersions()) {
//                final Set<Host> targetHosts = v.getTargetHosts();
//                final Version t_Version = new Version();
//                t_Version.setExecOpt(v.getExecOpt());
//                t_Version.setRepository(v.getRepository());
//                t_Version.setName(v.getName());
//                t_Version.setRevision(v.getRevision());
//                Set<Host> versionHosts = new HashSet<>();
//                for (final Host versionTargetHost : targetHosts) {
//                    if (host.getId().equals(versionTargetHost.getId())) {
//                        versionHosts.add(versionTargetHost);
//                    }
//                }
//                if (!versionHosts.isEmpty()) {
//                    t_Version.setTargetHosts(versionHosts);
//                    targetVersions.add(t_Version);
//                }
//            }
//        }
//        generateVarFile(app, path);
//        generateDeployFile(targetVersions, path);
//    }

    /**
     * 生成应用的待部署目标Host文件
     */
    private void generateHostFile() throws IOException {
        final File file = new File(getJobPath() + "/hosts");
        final File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        try (final FileWriter writer = new FileWriter(file)) {
            writer.append("\n\n");
            writer.append("[deploy_target]\n");
            for (final Long hid : hosts) {
                Host h = resources.hostDao.findOne(hid);
                if (h == null) {
                    logsQueue.offer("The specified host: "+hid + " not found");
                } else {
                    writer.append(h.getPrivateIp());
                    writer.append("\n");
                }
            }
            writer.flush();
        }
    }

//    /**
//     * 生成应用变量文件
//     */
//    private void generateVarFile(final App app, final String path) throws IOException {
//        final File file = new File(path + "/vars.yml");
//        final File parentFile = file.getParentFile();
//        if (!parentFile.exists()) {
//            parentFile.mkdirs();
//        }
//        try (final FileWriter writer = new FileWriter(file)) {
//            writer.append("product: ");
//            writer.append(app.getApptag());
//            writer.append("\n");
//            writer.append("deploy_path: ");
//            writer.append(app.getDeployPath());
//            writer.append("\n");
//            for (final AppVariable var : app.getVars()) {
//                writer.append(var.getName());
//                writer.append(": ");
//                writer.append(var.getValue());
//                writer.append("\n");
//            }
//            writer.flush();
//        }
//    }

//    private void generateSetenv(final Set<Version> versions, final String path) throws IOException {
//        final String pathname = CMD_PATH + app.getAppType().getName() + "/templates/setenv.sh";
//        final File file = new File(pathname);
//        final BufferedReader br = new BufferedReader(new FileReader(file));
//        final List<String> lists = new ArrayList<String>();
//        String str;
//        while ((str = br.readLine()) != null) {
//            lists.add(str);
//        }
//        br.close();
//
//        for (final Version version : versions) {
//            for (final Host h : version.getTargetHosts()) {
//                File setenv_file = new File(path + "/setenv_" + h.getPrivateIp() + ".sh");
//                BufferedWriter bw = new BufferedWriter(new FileWriter(setenv_file));
//
//                for (String strr : lists) {
//                    if (strr.contains("{{EXEC_OPTS}}")) {
//                        strr = strr.replaceAll("\\{\\{EXEC_OPTS\\}\\}", version.getExecOpt());
//                    }
//                    bw.write(strr);
//                    bw.newLine();
//                }
//                bw.flush();
//                bw.close();
//            }
//        }
//    }

//    private void generateDeployFile(final Set<Version> targetVersions, final String path) throws IOException {
//        for (final Version version : targetVersions) {
//            for (final Host host : version.getTargetHosts()) {
//                final BufferedWriter bw = new BufferedWriter(
//                        new FileWriter(new File(path + "/deploy_" + host.getPrivateIp() + ".yml")));
//
//                bw.write("- name: Copy the code from repository");
//                bw.newLine();
//                bw.write("  subversion: repo=svn://");
//                //bw.write(paramConfig.getSvnprivateaddr());
//                bw.write("/");
//                bw.write(app.getApptag());
//                bw.write("/");
//                bw.write(version.getRepository());
//                bw.write(" dest=/home/");
//                bw.write(app.getApptag());
//                if ("Tomcat".equalsIgnoreCase(app.getAppType().getName())) {
//                    bw.write("/tomcat/webapps/");
//                    bw.write(app.getDeployPath());
//                } else {
//                    bw.write("/");
//                    bw.write(app.getDeployPath());
//                }
//                bw.write(" force=no username=deploy password=unicorn4Felink");
//                bw.write(" revision=");
//                bw.write(version.getRevision());
//                bw.newLine();
//                bw.write("  become: yes");
//                bw.newLine();
//                bw.write("  become_user: ");
//                bw.write("\"" + app.getApptag() + "\"");
//
//                if ("deploy".equalsIgnoreCase(operation) && ("JavaServer".equalsIgnoreCase(app.getAppType().getName())
//                        || "Command".equalsIgnoreCase(app.getAppType().getName()))) {
//                    bw.newLine();
//                    bw.write("  notify: restart app");
//                }
//                bw.flush();
//                bw.close();
//            }
//        }
//    }

//    public void onSucceed(final String ret) {
//        if (operation.equals(TYPE_STATUS)) {
//            final Map<String, String> status = OperationUtil.parseStatusToJson(ret);
//            final OpViewMessage msg = new OpViewMessage(TYPE_STATUS, status);
//            final Map<WebSocketSession, IOperationViewWriter> writers = OperationWebSocketHandler
//                    .getViewWriters(app.getId());
//            for (final IOperationViewWriter writer : writers.values()) {
//                writer.write(msg);
//            }
//        }
//    }

//    public void onDeleteSucceed(final String ret) {
//        if (operation.equals(TYPE_DELETE)) {
//            final Map<WebSocketSession, IOperationViewWriter> writers = OperationWebSocketHandler
//                    .getViewWriters(app.getId());
//            String value = "";
//            if (StringUtils.isNotBlank(ret) && ret.contains("fatal")) {
//                value = "error";
//            } else {
//                final StringBuffer sb = new StringBuffer();
//                final Iterator<ManagedHost> iterator = hosts.iterator();
//                while (iterator.hasNext()) {
//                    sb.append(iterator.next().getId());
//                    sb.append(":");
//                }
//
//                if (sb.length() != 0) {
//                    value = sb.substring(0, sb.length() - 1);
//                }
//            }
//
//            final OpViewMessage successMsg = new OpViewMessage(TYPE_DELETE, value);
//            for (final IOperationViewWriter writer : writers.values()) {
//                writer.write(successMsg);
//            }
//        }
//    }


}