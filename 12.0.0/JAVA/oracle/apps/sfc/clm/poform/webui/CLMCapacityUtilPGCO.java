/*===========================================================================+
 |   Copyright (c) 2001, 2005 Oracle Corporation, Redwood Shores, CA, USA    |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY            1.1                                                      |
 +===========================================================================*/
package oracle.apps.sfc.clm.poform.webui;

import com.sun.java.util.collections.HashMap;

import oracle.apps.fnd.common.VersionInfo;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.OAWebBeanConstants;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;
import oracle.apps.sfc.clm.poform.server.CLMCreatePoAMImpl;

/**
 * Controller for ...
 */
public class CLMCapacityUtilPGCO extends OAControllerImpl
{
  public static final String RCS_ID="$Header$";
  public static final boolean RCS_ID_RECORDED =
        VersionInfo.recordClassVersion(RCS_ID, "%packagename%");

  /**
   * Layout and page setup logic for a region.
   * @param pageContext the current OA page context
   * @param webBean the web bean corresponding to the region
   */
  public void processRequest(OAPageContext pageContext, OAWebBean webBean)
  {
    super.processRequest(pageContext, webBean);
    
      CLMCreatePoAMImpl PoAM = (CLMCreatePoAMImpl)pageContext.getApplicationModule(webBean); 
      
      String    hdrid   =   pageContext.getParameter("PO_HDR_ID"); 
      String    clmId   =   pageContext.getParameter("PO_CLM_ID"); 
      
      PoAM.createCapacityUtilRow(hdrid,clmId);    
  }

  /**
   * Procedure to handle form submissions for form elements in
   * a region.
   * @param pageContext the current OA page context
   * @param webBean the web bean corresponding to the region
   */
  public void processFormRequest(OAPageContext pageContext, OAWebBean webBean)
  {
    super.processFormRequest(pageContext, webBean);
    CLMCreatePoAMImpl PoAM = (CLMCreatePoAMImpl)pageContext.getApplicationModule(webBean); 
    if (pageContext.getParameter("SaveBtn") != null) 
    {
        //check duplicate circuit id
        PoAM.getOADBTransaction().commit();
        HashMap     hm  =   new HashMap();
        hm.put("BackNavFrom",   "CapacityPage");     
        //   hm.put("CapacityMode","EditMode");  
        hm.put("pMode"      ,   "EditMode");
        
        pageContext.forwardImmediately  (
                                            "OA.jsp?page=/oracle/apps/sfc/clm/poform/webui/CLMPoCreatePG"   ,
                                            null                                                            ,
                                            OAWebBeanConstants.KEEP_MENU_CONTEXT                            ,
                                            null                                                            ,
                                            hm                                                              ,
                                            true                                                            , // retain AM
                                            OAWebBeanConstants.ADD_BREAD_CRUMB_NO
                                        );
    }     
    if (pageContext.getParameter("BackBtn") != null) 
    {
        HashMap     hm  =   new HashMap();        
        hm.put("BackNavFrom"    ,   "CapacityPage");     
        // hm.put("CapacityMode","EditMode");  
        hm.put("pMode"          ,   "EditMode");
        pageContext.forwardImmediately  (   
                                            "OA.jsp?page=/oracle/apps/sfc/clm/poform/webui/CLMPoCreatePG"   ,
                                            null                                                            ,
                                            OAWebBeanConstants.KEEP_MENU_CONTEXT                            ,
                                            null                                                            ,
                                            hm                                                              ,
                                            true                                                            , // retain AM
                                            OAWebBeanConstants.ADD_BREAD_CRUMB_NO
                                        );    
    }
  }
}
