package com.gitlab.pedrioko.core.view.util;

import com.gitlab.pedrioko.core.view.enums.MessageType;
import com.gitlab.pedrioko.domain.enumdomain.TipoUsuario;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.sys.ExecutionsCtrl;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

public class ZKUtil {

    private ZKUtil() {
    }

    public static void showMessage(String message) {
        if (ApplicationContextUtils.getBean(FHSessionUtil.class).getCurrentUser().getTipo() != TipoUsuario.ROLE_TURISTA)

            Clients.evalJavaScript("toastr." + getTypeMessage(MessageType.ERROR) + "('" + message + "')");
    }

    public static void showMessage(String message, MessageType type, TipoUsuario tipoUsuario) {
        if (ApplicationContextUtils.getBean(FHSessionUtil.class).getCurrentUser().getTipo() == tipoUsuario)
            Clients.evalJavaScript("toastr." + getTypeMessage(type) + "('" + message + "')");

    }

    public static void showMessage(String message, MessageType type) {
        Clients.evalJavaScript("toastr." + getTypeMessage(type) + "('" + message + "')");

    }

    private static String getTypeMessage(MessageType type) {
        String typetoast;
        switch (type) {
            case WARNING:
                typetoast = "warning";
                break;
            case ERROR:
                typetoast = "error";
                break;
            case SUCCESS:
                typetoast = "success";
                break;
            case INFO:
                typetoast = "info";
                break;
            default:
                typetoast = "info";
                break;
        }
        return typetoast;
    }

    public static boolean isMobile() {
        Execution current = Executions.getCurrent();
        return current != null && current.getBrowser("mobile") != null;
    }

    public static void showDialogWindow(Window window) {

        Page currentPage = ExecutionsCtrl.getCurrentCtrl().getCurrentPage();
        window.setClosable(true);
        window.setBorder("normal");
        window.setParent(currentPage.getFirstRoot());
        window.doModal();

    }
}
