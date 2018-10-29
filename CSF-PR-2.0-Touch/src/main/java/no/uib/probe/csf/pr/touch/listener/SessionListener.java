/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.uib.probe.csf.pr.touch.listener;

import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinSession;
import java.io.File;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;
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

    @Override
    public void sessionCreated(HttpSessionEvent hse) {
        session = hse.getSession();
        if (session != null) {
            timer = new Timer();
           timer.schedule(new RemindTask(),(5*60*60*1000));
           
        }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent hse) {
        Object path = hse.getSession().getAttribute("CsrfTokenFile");
        System.out.println("at -------------------------->>> session distroied  :-( " + path);
        if (path != null) {
            File file = new File(hse.getSession().getAttribute("CsrfTokenFile") + "");
            if (file.exists()) {
                file.delete();

            }
        }
    }

    class RemindTask extends TimerTask {

        @Override
        public void run() {
            System.out.println("at **********************************Time's up!**********************************");
            session.invalidate();
            timer.cancel(); //Not necessary because we call System.exit
            //Stops the AWT thread (and everything else)
        }
    };

}
