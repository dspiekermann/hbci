
/*  $Id: get_keyinfo.cpp 62 2008-10-22 17:03:26Z kleiner $

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

#include <stdlib.h>
#include <string.h>

#include "ctapi-tools.h"
#include "seccos.h"

bool SECCOS_getKeyInfo(unsigned char keynum,unsigned char keytype,unsigned char *buffer,size_t *size)
{
    unsigned char command[]=
    {
        SECCOS_CLA_EXT,
        SECCOS_INS_GET_KEYINFO,
        keytype,
        keynum,
        0x00,
    };
    unsigned short int len=300;
    unsigned char      *response=new unsigned char[len];
    
    unsigned short int status=CTAPI_performWithCard("getKeyInfo",5,command,&len,response);
    
    if (CTAPI_isOK(status)) {
        *size=len-2;
        memcpy(buffer,response,*size);
        
        delete response;
        return true;
    } else {
        delete response;
        return false;
    }
}
