/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.server.logic.operActivity.bean;

/**
 * 
 * @author Administrator
 */
public class OperActivityBeanBase
{
    private String strActivity;

    public boolean isActivityOpen(){
        
        
        return true;
    }
    
    public String getStrActivity()
    {
        return strActivity;
    }

    public void setStrActivity(String strActivity)
    {
        this.strActivity = strActivity;
    }
    
}
