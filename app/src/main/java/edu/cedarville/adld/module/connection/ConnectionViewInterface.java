package edu.cedarville.adld.module.connection;

public interface ConnectionViewInterface {

    void setStatusDisconnected();
    void setStatusConnecting();
    void setStatusConnectionFailed();
    void setStatusConnected(String name);
    void setStatusNotAvailable();
    void setClickableEnabled(boolean enabled);
}
