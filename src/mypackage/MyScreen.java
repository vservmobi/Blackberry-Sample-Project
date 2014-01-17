package mypackage;



import java.util.Enumeration;
import java.util.Vector;
import mobi.vserv.blackberry.ads.VservAd;
import mobi.vserv.blackberry.ads.VservController;
import mobi.vserv.blackberry.ads.VservManager;
import mobi.vserv.blackberry.ads.VservManagerInterface;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.Status;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class MyScreen extends MainScreen
{
	VservManager vservManager = null;
	VservController vservController = null;
    /**
     * Creates a new MyScreen object
     */
    public MyScreen()
    {        
        // Set the displayed title of the screen       
        setTitle("VservAdSample");
        VerticalFieldManager vFieldManager = new VerticalFieldManager(Field.FIELD_HCENTER|Field.FIELD_VCENTER);
        vFieldManager.add(new LabelField("Press Menu key --> For Display Ads.", Field.FIELD_HCENTER|Field.FIELD_VCENTER));
        add(vFieldManager);
        /*get Instance of VservManager Class*/
        vservManager = VservManager.getInstance();
    }
    
    /*------------Call for Fullscreen Ad in mid of Application-----------------*/
	private MenuItem billboardAd = new MenuItem(new String("Display Interstitial<displayAd()>"), 40, 40) {		
		public void run() {
			try{
			vservManager.setShowAt("in"); // setter for Ad show types -> start/end/in
	    	vservManager.setCaching(false); // setter for cache Ad 
			vservManager.displayAd("8063"); // display fullscreen Ad
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	/*-------------Call For Banner Ad---------------------------------------*/ 
    private MenuItem bannerAd = new MenuItem(new String("Display Banner<renderAd()>"), 40, 40) {		
		public void run() {
			try{
			VerticalFieldManager verticalFieldManager = new VerticalFieldManager(Field.FIELD_HCENTER|Field.FIELD_VCENTER);
			add(verticalFieldManager);
			vservController = vservManager.renderAd("20846", verticalFieldManager); // display banner Ad in application 
			vservController.setRefresh(30); // set refresh rate in secs for refreshing the banner Ads
			//vservController.setZone("Your Zone id");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	/*-------------Advanced Functionality for Cacheing Ad  and display it when user want (Interstitial)------------ */
    private MenuItem getAdBilloard = new MenuItem(new String("Display Interstitial<getAd()+overlay()>"), 40, 40) {
		public void run() {
			try{
				vservManager.getAd("8063", new VservManagerInterface() {
					public void onSuccess(VservAd arg0) {
						if(arg0 != null){
							/*display cached interstitial ad*/
							arg0.overlay();
						}
					}
					
					public void onNoFill() {
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							public void run() {
								Status.show("onNoFill");
							}
						});
						
					}
					public void onFailure() {
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							public void run() {
								Status.show("onFailure");
							}
						});
						
					}
				});
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	/*-------------Advanced Functionality for Cacheing Ad  and display it when user want (Banner)------------ */
    private MenuItem getAdBanner = new MenuItem(new String("Display Banner<getAd()+show()>"), 40, 40) {
		public void run() {
			try{
				vservManager.getAd("20846", new VservManagerInterface() {
					public void onSuccess(VservAd arg0) {
						if(arg0 != null){
							VerticalFieldManager manager = new VerticalFieldManager(Field.FIELD_HCENTER|Field.FIELD_VCENTER);
							add(manager);
							/*display cached banner Ad*/
							arg0.show(manager);
						}
					}
					
					public void onNoFill() {
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							public void run() {
								Status.show("onNoFill");
							}
						});
						
					}
					public void onFailure() {
						UiApplication.getUiApplication().invokeLater(new Runnable() {
							public void run() {
								Status.show("onFailure");
							}
						});
						
					}
				});
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
    public boolean onClose() {
    	return super.onClose();
    }
    
    protected void makeMenu(Menu menu, int instance) {
    	menu.deleteAll();
    	menu.add(billboardAd);
    	menu.add(bannerAd);
    	menu.add(getAdBilloard);
    	menu.add(getAdBanner);
    	super.makeMenu(menu, instance);
    }
    
    /*Pause Refresh of Banner Ads if Application on Background*/
    protected void deactivateApp() {
    	if(vservController != null)
    		vservController.pauseRefresh();
    }
    /*Resume Refresh of Banner Ads if Application on Foreground*/
    protected void activatedApp() {
    	if(vservController != null)
    		vservController.resumeRefresh();
    }
}
