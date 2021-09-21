package cn.soilove.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * JGIT工具类
 *
 * @author: Chen GuoLin
 * @create: 2021-09-19
 **/
@Slf4j
public class JGitUtils {

    /**
     * 克隆代码（无密码）
     * @param remoteUrl
     * @param branch
     * @param localPath
     * @return
     */
    public static boolean gitClone(String remoteUrl,String branch,String localPath) {
        try{
            log.info("[git_clone][无密码]开始下载，remoteUrl=" + remoteUrl + "，localPath=" + localPath);
            Git.cloneRepository()
                    .setURI(remoteUrl) // 设置远程地址
                    .setBranch(branch) // 设置分支
                    .setDirectory(new File(localPath)) // 设置本地路径
                    .call();
            log.info("[git_clone][无密码]下载完成！remoteUrl=" + remoteUrl + "，localPath=" + localPath);
            return true;
        }catch(Exception e){
            log.info("[git_clone][无密码]下载代码发生异常！remoteUrl=" + remoteUrl + "，localPath=" + localPath,e);
            return false;
        }
    }

    /**
     * 克隆代码（有密码）
     * @param remoteUrl
     * @param branch
     * @param localPath
     * @param username
     * @param password
     * @return
     */
    public static boolean gitClone(String remoteUrl,String branch,String localPath,String username,String password) {
        try{
            log.info("[git_clone][有密码]开始下载，remoteUrl=" + remoteUrl + "，localPath=" + localPath);
            // 创建链接下载代码
            Git git = Git.cloneRepository()
                    .setURI(remoteUrl) // 设置远程地址
                    .setBranch(branch) // 设置分支
                    .setDirectory(new File(localPath)) // 设置本地路径
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password)) // 设置权限验证
                    .call();
            // 关闭链接
            git.close();
            log.info("[git_clone][有密码]下载完成！remoteUrl=" + remoteUrl + "，localPath=" + localPath);
            return true;
        }catch(Exception e){
            log.error("[git_clone][有密码]下载代码发生异常！remoteUrl=" + remoteUrl + "，localPath=" + localPath,e);
            return false;
        }
    }

    /**
     * 更新代码（无密码）
     * @param localPath
     * @param branch
     * @return
     */
    public static boolean gitPull(String localPath, String branch) {
        try {
            log.info("[git_pull][无密码]开始更新代码，localPath=" + localPath);
            Git git = new Git(new FileRepository(localPath + "/.git"));
            PullResult result = git.pull()
                    .setRemoteBranchName(branch)
                    .call();
            if(result.isSuccessful()){
                log.info("[git_pull][无密码]更新代码完成，localPath=" + localPath);
                return true;
            }
            log.error("[git_pull][无密码]更新代码失败，localPath=" + localPath + "，result=" + result.toString());
            return false;
        } catch (IOException e) {
            log.error("[git_pull][无密码]更新代码发生IOException异常！localPath=" + localPath,e);
        } catch (GitAPIException e) {
            log.error("[git_pull][无密码]更新代码发生GitAPIException异常！localPath=" + localPath,e);
        }
        return false;
    }

    /**
     * 更新代码（有密码）
     * @param localPath
     * @param branch
     * @return
     */
    public static boolean gitPull(String localPath, String branch, String username,String password) {
        try {
            log.info("[git_pull][有密码]开始更新代码，localPath=" + localPath);
            Git git = new Git(new FileRepository(localPath + "/.git"));
            PullResult result = git.pull()
                    .setRemoteBranchName(branch)
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password)) // 设置权限验证
                    .call();
            if(result.isSuccessful()){
                log.info("[git_pull][有密码]更新代码完成，localPath=" + localPath);
                return true;
            }
            log.error("[git_pull][有密码]更新代码失败，localPath=" + localPath + "，result=" + result.toString());
            return false;
        } catch (IOException e) {
            log.error("[git_pull][有密码]更新代码发生IOException异常！localPath=" + localPath,e);
        } catch (GitAPIException e) {
            log.error("[git_pull][有密码]更新代码发生GitAPIException异常！localPath=" + localPath,e);
        }
        return false;
    }

    /**
     * 添加代码
     * @param localPath
     * @param filepattern 可为空
     * @return
     */
    public static boolean gitAdd(String localPath, String filepattern) {
        try {
            log.info("[git_add]开始添加代码，localPath=" + localPath);
            Git git = new Git(new FileRepository(localPath + "/.git"));
            // 指定filepattern的情况
            if(StringUtils.isNotBlank(filepattern)){
                git.add().addFilepattern(filepattern).call();
            }else{
                List<String> paths = new ArrayList<>();
                // 加载文件夹下的所有文件
                loadDirectoryFilePaths(localPath,paths);
                // 执行git命令
                AddCommand addCommand = git.add();
                for (String path : paths) {
                    addCommand.addFilepattern(path);
                }
                addCommand.call();
            }
            log.error("[git_add]添加代码成功，localPath=" + localPath);
            return false;
        } catch (IOException e) {
            log.error("[git_add]添加代码发生IOException异常！localPath=" + localPath,e);
        } catch (GitAPIException e) {
            log.error("[git_add]添加代码发生GitAPIException异常！localPath=" + localPath,e);
        }
        return false;
    }

    /**
     * 提交代码
     * @param localPath
     * @param message
     * @return
     */
    public static boolean gitCommit(String localPath, String message) {
        try {
            log.info("[git_commit]开始提交代码，localPath=" + localPath);
            Git git = new Git(new FileRepository(localPath + "/.git"));
            // 提交代码
            git.commit().setMessage(message).call();
            log.error("[git_commit]提交代码成功，localPath=" + localPath);
            return false;
        } catch (IOException e) {
            log.error("[git_commit]提交代码发生IOException异常！localPath=" + localPath,e);
        } catch (GitAPIException e) {
            log.error("[git_commit]提交代码发生GitAPIException异常！localPath=" + localPath,e);
        }
        return false;
    }

    /**
     * 发布代码
     * @param localPath
     * @param username
     * @param password
     * @return
     */
    public static boolean gitPush(String localPath, String username,String password) {
        try {
            log.info("[git_push]开始发布代码，localPath=" + localPath);
            Git git = new Git(new FileRepository(localPath + "/.git"));
            // 提交代码
            git.push().setRemote("origin")
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password)) // 设置权限验证
                    .call();
            log.error("[git_push]发布代码成功，localPath=" + localPath);
            return false;
        } catch (IOException e) {
            log.error("[git_push]发布代码发生IOException异常！localPath=" + localPath,e);
        } catch (GitAPIException e) {
            log.error("[git_push]发布代码发生GitAPIException异常！localPath=" + localPath,e);
        }
        return false;
    }

    /**
     * 加载文件路径下的所有文件
     * @param directoryPath
     * @param paths
     */
    private static void loadDirectoryFilePaths(String directoryPath,List<String> paths){
        File directory = new File(directoryPath);
        for (String filePath : directory.list()){
            if (".git".equals(filePath)){
                continue;
            }
            File directory4Item = new File(filePath);
            if(directory4Item.isDirectory()){
                // 递归
                loadDirectoryFilePaths(filePath,paths);
            }
            log.info("找到文件：" + filePath);
            paths.add(filePath);
        }
    }


    public static void main(String[] args) {
        String giturl = "http://u.32e.co:8122/git/root/test02.git";
        String lourl = PathUtils.getProjectPath() + "/template/bp";
        gitClone(giturl,"master",lourl,"root","root");
        gitPull(lourl,"master","root","root");
    }

}
