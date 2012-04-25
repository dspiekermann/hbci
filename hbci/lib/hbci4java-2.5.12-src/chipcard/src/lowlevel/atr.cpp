
/*  $Id: atr.cpp 62 2008-10-22 17:03:26Z kleiner $

    This file is part of HBCI4Java
    Copyright (C) 2001-2007  Stefan Palme

    HBCI4Java is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    HBCI4Java is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

#include <stdio.h>
#include <string.h>

#include "atr.h"
#include "ctapi-tools.h"

void analyzeATR(unsigned char *atr,size_t len)
{
    char          temp[1024];
    unsigned char ts=atr[0];
    unsigned char t0=atr[1];
    
    if (ts==0x3F)
        CTAPI_log("ATR: using inverse coding convention");
    else if (ts==0x3B)
        CTAPI_log("ATR: using direct coding convention");
    else
        CTAPI_log("ATR: unknown coding convention!");
    
    int           posi=1;
    unsigned char t=atr[posi];
    int           idx=1;
    
    while (t&0xF0) {
        if (t&0x10) {
            sprintf(temp,"TA%i present",idx);
            CTAPI_log(temp);
            posi++;
        }

        if (t&0x20) {
            sprintf(temp,"TB%i present",idx);
            CTAPI_log(temp);
            posi++;
        }

        if (t&0x40) {
            sprintf(temp,"TC%i present",idx);
            CTAPI_log(temp);
            posi++;
        }

        if (t&0x80) {
            sprintf(temp,"TD%i present",idx);
            CTAPI_log(temp);
            posi++;
            t=atr[posi];
        } else {
            t=0;
        }
        
        idx++;
    }
    
    unsigned char nof_hisBytes=t0&0x0F;
    sprintf(temp,"there are %i historical bytes: ",nof_hisBytes);
    
    for (int i=0;i<nof_hisBytes;i++) {
        unsigned char ch=atr[posi+1+i];
        sprintf(temp+strlen(temp),"%c",(ch<0x20)?'.':ch);
    }
    CTAPI_log(temp);
}
