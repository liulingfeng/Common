package com.llf.common.entity;

import java.io.Serializable;

/**
 * Created by llf on 2017/4/21.
 */

public class ApplicationEntity implements Serializable {

    /**
     * name : 秀趣
     * version : 1
     * changelog : 初次上传，基本功能可以体验
     * updated_at : 1492680055
     * versionShort : 1.0
     * build : 1
     * installUrl : http://download.fir.im/v2/app/install/58f87d50959d6904280005a3?download_token=5f92dec5479f505e3c5e571075297e64&source=update
     * install_url : http://download.fir.im/v2/app/install/58f87d50959d6904280005a3?download_token=5f92dec5479f505e3c5e571075297e64&source=update
     * direct_install_url : http://download.fir.im/v2/app/install/58f87d50959d6904280005a3?download_token=5f92dec5479f505e3c5e571075297e64&source=update
     * update_url : http://fir.im/6s7z
     * binary : {"fsize":3933914}
     */

    private String name;
    private String version;
    private String changelog;
    private int updated_at;
    private String versionShort;
    private String build;
    private String installUrl;
    private String install_url;
    private String direct_install_url;
    private String update_url;
    private BinaryBean binary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getChangelog() {
        return changelog;
    }

    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }

    public int getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(int updated_at) {
        this.updated_at = updated_at;
    }

    public String getVersionShort() {
        return versionShort;
    }

    public void setVersionShort(String versionShort) {
        this.versionShort = versionShort;
    }

    public String getBuild() {
        return build;
    }

    public void setBuild(String build) {
        this.build = build;
    }

    public String getInstallUrl() {
        return installUrl;
    }

    public void setInstallUrl(String installUrl) {
        this.installUrl = installUrl;
    }

    public String getInstall_url() {
        return install_url;
    }

    public void setInstall_url(String install_url) {
        this.install_url = install_url;
    }

    public String getDirect_install_url() {
        return direct_install_url;
    }

    public void setDirect_install_url(String direct_install_url) {
        this.direct_install_url = direct_install_url;
    }

    public String getUpdate_url() {
        return update_url;
    }

    public void setUpdate_url(String update_url) {
        this.update_url = update_url;
    }

    public BinaryBean getBinary() {
        return binary;
    }

    public void setBinary(BinaryBean binary) {
        this.binary = binary;
    }

    public static class BinaryBean implements Serializable {
        /**
         * fsize : 3933914
         */

        private int fsize;

        public int getFsize() {
            return fsize;
        }

        public void setFsize(int fsize) {
            this.fsize = fsize;
        }
    }


    @Override
    public String toString() {
        return "应用名字:" + name + "版本:" + version + "日志" + changelog;
    }
}
