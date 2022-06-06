/*===========================================================================+
 |   Copyright (c) 2001, 2005 Oracle Corporation, Redwood Shores, CA, USA    |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 +===========================================================================*/
package oracle.apps.sfc.clm.PaymentApproval.webui;

import oracle.apps.fnd.common.VersionInfo;
import oracle.apps.fnd.framework.OAApplicationModule;
import oracle.apps.fnd.framework.OAViewObject;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.OAWebBeanConstants;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;

import oracle.apps.sfc.clm.PaymentApproval.server.xxsifyPaymentApprovalAMImpl;

import oracle.jbo.ViewObject;

/**
 * Controller for ...
 */
public class ClmInvoiceDetailsCO extends OAControllerImpl
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
    xxsifyPaymentApprovalAMImpl am   =  (xxsifyPaymentApprovalAMImpl)pageContext.getApplicationModule(webBean);
    OAViewObject                vo   =  am.getCLMInvDetailsVO(); 
    
    String    clmid  =  pageContext.getParameter("param1");   
    String    invNo  =  pageContext.getParameter("param2");
    
    System.out.println("param1    --> "   +   clmid);
    System.out.println("param2    --> "   +   invNo);
    
    vo.setWhereClause(null);
    vo.setWhereClauseParams(null);
    vo.setWhereClause("clm_id=:1 and clm_invoice_number=:2");
    vo.setWhereClauseParam(0,clmid);
    vo.setWhereClauseParam(1,invNo);
    vo.executeQuery();
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
    if (pageContext.getParameter("backBtn") != null)
    {
       pageContext.setForwardURL("OA.jsp?page=/oracle/apps/sfc/clm/PaymentApproval/webui/xxsifyPaymentApprovalPG"   , 
                                  null                                                                              , 
                                  OAWebBeanConstants.KEEP_MENU_CONTEXT                                              , 
                                  null                                                                              , 
                                  null                                                                              , 
                                  false                                                                             , 
                                  OAWebBeanConstants.ADD_BREAD_CRUMB_NO                                             , 
                                  OAWebBeanConstants.IGNORE_MESSAGES
                                );
    }
  }

}
