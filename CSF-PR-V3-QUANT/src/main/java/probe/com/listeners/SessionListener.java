/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.listeners;

import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import java.io.File;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author Yehia Farag
 */
public class SessionListener implements HttpSessionListener, Serializable {

    private Timer timer;
    private HttpSession session;
    private String userFolderUrl;

    /**
     *
     * @param hse
     */
    @Override
    public void sessionCreated(HttpSessionEvent hse) {
        session = hse.getSession();
        String sessionId = session.getId();
        String csfFolderUrl = session.getServletContext().getInitParameter("filesURL");
        userFolderUrl = csfFolderUrl + "\\" + sessionId;        
        if (session != null) {
            timer = new Timer();
            timer.schedule(new RemindTask(),(5 * 60 * 1000 * 60));//); //* 60 * 60 * 1000
        }
        

    }

    private void deleteFiles(File f) {
        if (f.isDirectory() && f.listFiles().length > 0) {
            for (File subFile : f.listFiles()) {
                deleteFiles(subFile);

            }

        } else if (!f.getName().equalsIgnoreCase("CSF_Files")) {
            f.delete();
        }

    }

    /**
     *
     * @param hse
     */
    @Override
    public void sessionDestroyed(HttpSessionEvent hse) {
        System.gc();
        deleteFiles(new File(userFolderUrl));
    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {
            timer.cancel();
            System.out.println("Time's up!");
            session.invalidate();

//            VaadinSession.getCurrent().close();
        }
    };

}
