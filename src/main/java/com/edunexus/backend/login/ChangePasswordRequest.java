package com.edunexus.backend.login;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangePasswordRequest {

    @JsonProperty("oldPassword")
    private String oldPassword;

    @JsonProperty("newPassword")
    private String newPassword;

    public String getOldPassword() { return oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
