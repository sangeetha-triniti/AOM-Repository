/*===========================================================================+
 |   Copyright (c) 2001, 2005 Oracle Corporation, Redwood Shores, CA, USA    |
 |                         All rights reserved.                              |
 +===========================================================================+
 |  HISTORY                                                                  |
 +===========================================================================*/
package oracle.apps.sfc.clm.PaymentApproval.webui;

import com.sun.java.util.collections.HashMap;

import java.sql.CallableStatement;
import java.sql.Types;

import java.util.Vector;

import oracle.apps.fnd.common.VersionInfo;
import oracle.apps.fnd.cp.request.ConcurrentRequest;
import oracle.apps.fnd.cp.request.RequestSubmissionException;
import oracle.apps.fnd.framework.OAApplicationModule;
import oracle.apps.fnd.framework.OAException;
import oracle.apps.fnd.framework.OAViewObject;
import oracle.apps.fnd.framework.server.OADBTransaction;
import oracle.apps.fnd.framework.webui.OAControllerImpl;
import oracle.apps.fnd.framework.webui.OADialogPage;
import oracle.apps.fnd.framework.webui.OAPageContext;
import oracle.apps.fnd.framework.webui.OAWebBeanConstants;
import oracle.apps.fnd.framework.webui.beans.OAWebBean;

import oracle.apps.sfc.clm.PaymentApproval.server.CLMSearchNewVORowImpl;
import oracle.apps.sfc.clm.PaymentApproval.server.XXSifySearchNewVORowImpl;
import oracle.apps.sfc.clm.PaymentApproval.server.xxsifyPaymentApprovalAMImpl;
import oracle.apps.sfc.clm.PaymentApproval.server.xxsifySearchVORowImpl;

import oracle.jbo.Row;

/**
 * Controller for ...
 */
public class xxsifyPaymentApprovalCO extends OAControllerImpl
{
  public static final String RCS_ID="$Header$";
  public static final boolean RCS_ID_RECORDED =
        VersionInfo.recordClassVersion(RCS_ID, "%packagename%");
        
        
  /**
   * Layout and page setup logic for a region.
   * @param pageContext the current OA page context
   * @param webBean the web bean corresponding to the region
   */
   int      approver_lvl    =   0;
   String   approvedClmids  =   "";
   String   rejectedClmids  =   ""; 
  public void processRequest(OAPageContext pageContext, OAWebBean webBean)
  {
    super.processRequest(pageContext, webBean);
    xxsifyPaymentApprovalAMImpl am  = (xxsifyPaymentApprovalAMImpl)pageContext.getApplicationModule(webBean);
    OAViewObject                vo  =   (OAViewObject)am.getCLMSearchNewVO1();
    String              stmt    = "BEGIN xxsify_clm_pay_wf_valid_pkg.xxsify_clm_get_approver_lvl(:1,:2); end;";
    CallableStatement   cs      =  am.getOADBTransaction().createCallableStatement(stmt, 1);
    try 
    {
        //cs.setString(1,"GOPARANI.RAJAIAH");
        cs.setString(1,pageContext.getUserName());
        cs.registerOutParameter(2,Types.INTEGER);
        cs.execute();
        approver_lvl=cs.getInt(2);
        cs.close();
    } 
    catch (Exception e) 
    {
        throw new OAException("Error while getting approver"+e.getMessage() ,OAException.ERROR);
    }       
         
    System.out.println("lvl="+approver_lvl);
    if( approver_lvl == 1)
    {
        
//      String status="Submitted";
        vo.setWhereClause("INVOICE_WF_STATUS='Submitted'");//+status);
        vo.executeQuery();
        System.out.println("  Query  "+vo.getQuery());
        /*  OAException     confirmMessage  =  new OAException("You do not have access to this page.");
            OADialogPage    dialogPage      =  new OADialogPage(OAException.ERROR, confirmMessage, null, "OA.jsp?page=/oracle/apps/fnd/framework/navigate/webui/HomePG", null);
            pageContext.redirectToDialogPage(dialogPage);*/
    }
    else if( approver_lvl == 2)
    {
//      String          status  =   "Approver1 Approved";
        vo.setWhereClause(null);
        vo.setWhereClause("INVOICE_WF_STATUS='Approver1 Approved'");
        vo.executeQuery();
        
        /*  OAException     confirmMessage  =  new OAException("You do not have access to this page.");
            OADialogPage    dialogPage      = new OADialogPage(OAException.ERROR, confirmMessage,  null,  "OA.jsp?page=/oracle/apps/fnd/framework/navigate/webui/HomePG",  null);
            pageContext.redirectToDialogPage(dialogPage);*/
    }
// approver_lvl=2;
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
    xxsifyPaymentApprovalAMImpl am  = (xxsifyPaymentApprovalAMImpl)pageContext.getApplicationModule(webBean);
    OAViewObject                vo  =   (OAViewObject)am.getCLMSearchNewVO1();
    if (pageContext.getParameter("searchBtn") != null)
    {
        String clmid =(String)pageContext.getParameter("clmid");  
        if( approver_lvl == 1)
        {
            String status   =   "Submitted";
            vo.setWhereClause(null);
            vo.setWhereClause("INVOICE_WF_STATUS=:1 and clm_id=nvl(:2,clm_id)");//+status);
            vo.setWhereClauseParams(null);
            vo.setWhereClauseParam(0,status);
            vo.setWhereClauseParam(1,clmid);
            vo.executeQuery();
        }
        if( approver_lvl == 2)
        {
            String status       =   "Approver1 Approved";
            vo.setWhereClause("INVOICE_WF_STATUS=:1 and clm_id=nvl(:2,clm_id)");//+status);
            vo.setWhereClauseParams(null);
            vo.setWhereClauseParam(0,status);
            vo.setWhereClauseParam(1,clmid);
            vo.executeQuery();
        }
    }
    if (pageContext.getParameter("cancelBtn") != null)
    {
        pageContext.setForwardURL(  "OA.jsp?page=/oracle/apps/fnd/framework/navigate/webui/HomePG"  , 
                                    null                                                            , 
                                    OAWebBeanConstants.KEEP_MENU_CONTEXT                            , 
                                    null                                                            , 
                                    null                                                            , 
                                    true                                                            , 
                                    OAWebBeanConstants.ADD_BREAD_CRUMB_NO                           , 
                                    OAWebBeanConstants.IGNORE_MESSAGES
                                );
    }
    if (pageContext.getParameter("ApproveBtn") != null)
    {
        OAViewObject    VO  =   (OAViewObject)am.getCLMSearchNewVO1();
        Row[]           row =   VO.getFilteredRows("approveSelectFlag","Y");
        
        System.out.println("Total Rows: "+VO.getRowCount());
        System.out.println("Selected Rows: "+row.length);
    
        int cnt         =   0;
//      int batchno     =   0;
        int batchno     =   am.getOADBTransaction().getSequenceValue("xxsify_clm_inv_batch_s").intValue();
//      Row row[]       =   VO.getAllRowsInRange();
        for (int i=0;i<row.length;i++)  
        {
            //  xxsifySearchVORowImpl rowi = (xxsifySearchVORowImpl)row[i];
            CLMSearchNewVORowImpl rowi  =   (CLMSearchNewVORowImpl)row[i];
            approvedClmids              =   rowi.getClmId() +   "," +   approvedClmids;
            if (rowi.getapproveSelectFlag() != null && rowi.getapproveSelectFlag().equals("Y"))
            {
                cnt =   1;
                String              stmt    = "BEGIN xxsify_clm_pay_wf_valid_pkg.xxsify_clm_approval_status_upd(:1,:2,:3,:4,:5,:6,:7); end;";
                CallableStatement   cs      =  am.getOADBTransaction().createCallableStatement(stmt, 1);
                try
                {
                    cs.setString(1, rowi.getClmId().toString());
                    cs.setString(2, rowi.getClmInvoiceNumber());
                    cs.setInt(3,approver_lvl);
                    cs.setString(4,"Approved");
                    cs.setInt(5,pageContext.getUserId());
                    //   cs.registerOutParameter(6,Types.INTEGER);
                    cs.setInt(6,batchno);
                    cs.setString(7,"");
                    cs.execute(); 
                    //    batchno=cs.getInt(6);
                    cs.close();
                } 
                catch (Exception e)
                {
                    throw new OAException("Error while approving"+e.getMessage(),OAException.ERROR);
                }        
            } // end if
        }   // end for lop    
        if (cnt == 0)
        {
            throw new OAException("Please Select a Record to proceed..",OAException.ERROR);
        }
        if ( approver_lvl == 1) 
        {
            //approver2 wf call
            System.out.println("inside wf 2 call"+  approvedClmids);
            String stmt = "BEGIN xxsify_clm_pay_wf_valid_pkg.sify_clm_pay_approver2_wf_call(:1,:2,:3); end;";
            CallableStatement cs = am.getOADBTransaction().createCallableStatement(stmt, 1);
            try 
            {
                cs.setString(1,approvedClmids );
                cs.setInt(2,batchno);                 
                cs.setString(3,pageContext.getUserName());
                cs.execute(); 
                cs.close();
            } 
            catch (Exception e) 
            {
                throw new OAException("Error while calling approver2 wf"+e.getMessage());
            }        
        }
        if ( approver_lvl == 2 ) 
        {
        /*      String              stmt    = "BEGIN xxsify_clm_pay_wf_valid_pkg.xxsify_clm_submitting_cp(:1,:2); end;";
                CallableStatement   cs      =  am.getOADBTransaction().createCallableStatement(stmt, 1);
                try 
                {
                    // cs.setString(1,approvedClmids );
                    cs.setInt(1,pageContext.getUserId());
                    cs.setInt(2,batchno);
                    cs.execute(); 
                    cs.close();
                } 
                catch (Exception e) 
                {
                    throw new OAException("Error while submitting standard cp"+e.getMessage());
                }   
        */
            int        ln_request_id   =   0;
            String     rId             =   ""; //   String BwReqId=HdrVO.first().getAttribute("BandWidthReqId")+"";
            try
            {
                OADBTransaction         trx     = am.getOADBTransaction();
                java.sql.Connection     conn    = trx.getJdbcConnection();
                ConcurrentRequest       cr      = new ConcurrentRequest(conn);
                
                String                  cpApplName  = "SFC";
                String                  cpPrgName   = "XXSIFY_CLM_INVOICE_CP";
                String                  cpPrgDesc   = null;
                Vector                  cpArgs      = new Vector();
                //  cpArgs.addElement(pageContext.getUserId());
                cpArgs.addElement(batchno+"");
                //   System.out.println("fileName - " +fileName);
                ln_request_id = cr.submitRequest(cpApplName, cpPrgName, cpPrgDesc, null, false, cpArgs);
                trx.commit();
                System.out.println("requestId is - " +ln_request_id);
                rId = ln_request_id+"";  
            } // Try
            catch(RequestSubmissionException ex)
            {
                OAException exc = new OAException(ex.getMessage());
                throw exc;
            } 
        }
        am.getOADBTransaction().commit();
        OAException confirmMessage =  new OAException("Approved Successfully..");
        OADialogPage dialogPage =  new OADialogPage(OAException.CONFIRMATION, confirmMessage,  null,  "OA.jsp?page=/oracle/apps/fnd/framework/navigate/webui/HomePG",  null);
        pageContext.redirectToDialogPage(dialogPage);     
    } // end if approve btn check
    if (pageContext.getParameter("RejectBtn") != null)
    {
        OAViewObject    VO  =   (OAViewObject)am.getCLMSearchNewVO1();
        Row[]           row =   VO.getFilteredRows("approveSelectFlag","Y");
        
        System.out.println("Total Rows: "+VO.getRowCount());
        System.out.println("Selected Rows: "+row.length);
        
        //   Row row[] = VO.getAllRowsInRange();
        if (pageContext.getParameter("reasonId") == null || pageContext.getParameter("reasonId").equalsIgnoreCase(""))
        {
            throw new OAException("Please Enter Reason For Rejection",OAException.ERROR);
        }
        int batchno =   am.getOADBTransaction().getSequenceValue("xxsify_clm_inv_batch_s").intValue();
        for (int i=0;i<row.length;i++)  
        {
            CLMSearchNewVORowImpl rowi  =   (CLMSearchNewVORowImpl)row[i];
            rejectedClmids              =   rowi.getClmId()+","+rejectedClmids;
            if (rowi.getapproveSelectFlag() != null && rowi.getapproveSelectFlag().equals("Y"))
            {
                String              stmt    = "BEGIN xxsify_clm_pay_wf_valid_pkg.xxsify_clm_approval_status_upd(:1,:2,:3,:4,:5,:6,:7); end;";
                CallableStatement   cs      =  am.getOADBTransaction().createCallableStatement(stmt, 1);
                try
                {
                    cs.setString(1, rowi.getClmId().toString());
                    cs.setString(2, rowi.getClmInvoiceNumber());
                    cs.setInt(3,approver_lvl);
                    cs.setString(4,"Rejected");
                    cs.setInt(5,pageContext.getUserId());
                    cs.setInt(6,batchno);
                    cs.setString(7,pageContext.getParameter("reasonId"));
                    cs.execute(); 
                    
                    cs.close();
                    } 
                catch (Exception e) 
                {
                    e.printStackTrace();
                }        
            } // end if
        }// end for lop    
        //  if ( approver_lvl == 1 ) {
        //Requester wf call
        System.out.println("inside wf 2 call"+rejectedClmids);
        String              stmt    = "BEGIN xxsify_clm_pay_wf_valid_pkg.sify_clm_pay_requester_wf_call(:1,:2,:3,:4); end;";
        CallableStatement   cs      =  am.getOADBTransaction().createCallableStatement(stmt, 1);
        try 
        {
            cs.setString(1,rejectedClmids );
            cs.setInt(2,batchno);
            cs.setString(3,pageContext.getUserName());
            cs.setString(4,"Rejected");
            cs.execute(); 
            
            cs.close();
        } 
        catch (Exception e) 
        {
            throw new OAException("Error while calling approver2 wf"+e.getMessage());
        }        
        OAException     confirmMessage  =   new OAException("Rejected Successfully..");
        OADialogPage    dialogPage      =   new OADialogPage(OAException.CONFIRMATION, confirmMessage,  null,  "OA.jsp?page=/oracle/apps/fnd/framework/navigate/webui/HomePG",  null);
        pageContext.redirectToDialogPage(dialogPage);
    } //end if reject btn check
  } 
}
