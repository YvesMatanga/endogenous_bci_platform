/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.user.bci;

/**
 *
 * @author Yves Matanga
 */
public class EegChannelSelector {
    
 boolean fp1_used;
 boolean fp2_used;
 boolean af3_used;
 boolean af4_used;
 boolean f7_used;
 boolean f3_used;
 boolean fz_used;
 boolean f4_used;

 boolean f8_used;
 boolean fc5_used;
 boolean fc1_used;
 boolean fc2_used;
 boolean fc6_used;
 boolean t7_used;
 boolean c3_used;
 boolean cz_used;

 boolean c4_used;
 boolean t8_used;
 boolean cp5_used;
 boolean cp1_used;
 boolean cp2_used;
 boolean cp6_used;
 boolean p7_used;
 boolean p3_used;

 boolean pz_used;
 boolean p4_used;
 boolean p8_used;
 boolean po7_used;
 boolean po3_used;
 boolean po4_used;
 boolean po8_used;
 boolean oz_used;
   //paremetrized constructor
  public EegChannelSelector(boolean fp1_used, boolean fp2_used, boolean af3_used, boolean af4_used, boolean f7_used, boolean f3_used, boolean fz_used, boolean f4_used, boolean f8_used, boolean fc5_used, boolean fc1_used, boolean fc2_used, boolean fc6_used, boolean t7_used, boolean c3_used, boolean cz_used, boolean c4_used, boolean t8_used, boolean cp5_used, boolean cp1_used, boolean cp2_used, boolean cp6_used, boolean p7_used, boolean p3_used, boolean pz_used, boolean p4_used, boolean p8_used, boolean po7_used, boolean po3_used, boolean po4_used, boolean po8_used, boolean oz_used) {
        this.fp1_used = fp1_used;
        this.fp2_used = fp2_used;
        this.af3_used = af3_used;
        this.af4_used = af4_used;
        this.f7_used = f7_used;
        this.f3_used = f3_used;
        this.fz_used = fz_used;
        this.f4_used = f4_used;
        this.f8_used = f8_used;
        this.fc5_used = fc5_used;
        this.fc1_used = fc1_used;
        this.fc2_used = fc2_used;
        this.fc6_used = fc6_used;
        this.t7_used = t7_used;
        this.c3_used = c3_used;
        this.cz_used = cz_used;
        this.c4_used = c4_used;
        this.t8_used = t8_used;
        this.cp5_used = cp5_used;
        this.cp1_used = cp1_used;
        this.cp2_used = cp2_used;
        this.cp6_used = cp6_used;
        this.p7_used = p7_used;
        this.p3_used = p3_used;
        this.pz_used = pz_used;
        this.p4_used = p4_used;
        this.p8_used = p8_used;
        this.po7_used = po7_used;
        this.po3_used = po3_used;
        this.po4_used = po4_used;
        this.po8_used = po8_used;
        this.oz_used = oz_used;
    }
   //default constructor
  public EegChannelSelector(){
        this.fp1_used = true;
        this.fp2_used = true;
        this.af3_used = true;
        this.af4_used = true;
        this.f7_used = true;
        this.f3_used = true;
        this.fz_used = true;
        this.f4_used = true;
        this.f8_used = true;
        this.fc5_used = true;
        this.fc1_used = true;
        this.fc2_used = true;
        this.fc6_used = true;
        this.t7_used = true;
        this.c3_used = true;
        this.cz_used = true;
        this.c4_used = true;
        this.t8_used = true;
        this.cp5_used = true;
        this.cp1_used = true;
        this.cp2_used = true;
        this.cp6_used = true;
        this.p7_used = true;
        this.p3_used = true;
        this.pz_used = true;
        this.p4_used = true;
        this.p8_used = true;
        this.po7_used = true;
        this.po3_used = true;
        this.po4_used = true;
        this.po8_used = true;
        this.oz_used = true;
     }

    //
    public boolean isFp1_used() {
        return fp1_used;
    }

    public void setFp1_used(boolean fp1_used) {
        this.fp1_used = fp1_used;
    }

    public boolean isFp2_used() {
        return fp2_used;
    }

    public void setFp2_used(boolean fp2_used) {
        this.fp2_used = fp2_used;
    }

    public boolean isAf3_used() {
        return af3_used;
    }

    public void setAf3_used(boolean af3_used) {
        this.af3_used = af3_used;
    }

    public boolean isAf4_used() {
        return af4_used;
    }

    public void setAf4_used(boolean af4_used) {
        this.af4_used = af4_used;
    }

    public boolean isF7_used() {
        return f7_used;
    }

    public void setF7_used(boolean f7_used) {
        this.f7_used = f7_used;
    }

    public boolean isF3_used() {
        return f3_used;
    }

    public void setF3_used(boolean f3_used) {
        this.f3_used = f3_used;
    }

    public boolean isFz_used() {
        return fz_used;
    }

    public void setFz_used(boolean fz_used) {
        this.fz_used = fz_used;
    }

    public boolean isF4_used() {
        return f4_used;
    }

    public void setF4_used(boolean f4_used) {
        this.f4_used = f4_used;
    }

    public boolean isF8_used() {
        return f8_used;
    }

    public void setF8_used(boolean f8_used) {
        this.f8_used = f8_used;
    }

    public boolean isFc5_used() {
        return fc5_used;
    }

    public void setFc5_used(boolean fc5_used) {
        this.fc5_used = fc5_used;
    }

    public boolean isFc1_used() {
        return fc1_used;
    }

    public void setFc1_used(boolean fc1_used) {
        this.fc1_used = fc1_used;
    }

    public boolean isFc2_used() {
        return fc2_used;
    }

    public void setFc2_used(boolean fc2_used) {
        this.fc2_used = fc2_used;
    }

    public boolean isFc6_used() {
        return fc6_used;
    }

    public void setFc6_used(boolean fc6_used) {
        this.fc6_used = fc6_used;
    }

    public boolean isT7_used() {
        return t7_used;
    }

    public void setT7_used(boolean t7_used) {
        this.t7_used = t7_used;
    }

    public boolean isC3_used() {
        return c3_used;
    }

    public void setC3_used(boolean c3_used) {
        this.c3_used = c3_used;
    }

    public boolean isCz_used() {
        return cz_used;
    }

    public void setCz_used(boolean cz_used) {
        this.cz_used = cz_used;
    }

    public boolean isC4_used() {
        return c4_used;
    }

    public void setC4_used(boolean c4_used) {
        this.c4_used = c4_used;
    }

    public boolean isT8_used() {
        return t8_used;
    }

    public void setT8_used(boolean t8_used) {
        this.t8_used = t8_used;
    }

    public boolean isCp5_used() {
        return cp5_used;
    }

    public void setCp5_used(boolean cp5_used) {
        this.cp5_used = cp5_used;
    }

    public boolean isCp1_used() {
        return cp1_used;
    }

    public void setCp1_used(boolean cp1_used) {
        this.cp1_used = cp1_used;
    }

    public boolean isCp2_used() {
        return cp2_used;
    }

    public void setCp2_used(boolean cp2_used) {
        this.cp2_used = cp2_used;
    }

    public boolean isCp6_used() {
        return cp6_used;
    }

    public void setCp6_used(boolean cp6_used) {
        this.cp6_used = cp6_used;
    }

    public boolean isP7_used() {
        return p7_used;
    }

    public void setP7_used(boolean p7_used) {
        this.p7_used = p7_used;
    }

    public boolean isP3_used() {
        return p3_used;
    }

    public void setP3_used(boolean p3_used) {
        this.p3_used = p3_used;
    }

    public boolean isPz_used() {
        return pz_used;
    }

    public void setPz_used(boolean pz_used) {
        this.pz_used = pz_used;
    }

    public boolean isP4_used() {
        return p4_used;
    }

    public void setP4_used(boolean p4_used) {
        this.p4_used = p4_used;
    }

    public boolean isP8_used() {
        return p8_used;
    }

    public void setP8_used(boolean p8_used) {
        this.p8_used = p8_used;
    }

    public boolean isPo7_used() {
        return po7_used;
    }

    public void setPo7_used(boolean po7_used) {
        this.po7_used = po7_used;
    }

    public boolean isPo3_used() {
        return po3_used;
    }

    public void setPo3_used(boolean po3_used) {
        this.po3_used = po3_used;
    }

    public boolean isPo4_used() {
        return po4_used;
    }

    public void setPo4_used(boolean po4_used) {
        this.po4_used = po4_used;
    }

    public boolean isPo8_used() {
        return po8_used;
    }

    public void setPo8_used(boolean po8_used) {
        this.po8_used = po8_used;
    }

    public boolean isOz_used() {
        return oz_used;
    }

    public void setOz_used(boolean oz_used) {
        this.oz_used = oz_used;
    }
  
 
}
