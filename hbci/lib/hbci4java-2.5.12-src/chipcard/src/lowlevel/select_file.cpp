
/*  $Id: select_file.cpp 129 2009-04-08 06:11:09Z kleiner $

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

#include <string.h>
#include <stdio.h>

#include "ctapi-tools.h"
#include "seccos.h"
#include "tools.h"

void* extractSelectResult(unsigned short int len,unsigned char *response,unsigned char returntype)
{
    void* ret=NULL;
    
    switch (returntype) {
        case SECCOS_SELECT_RET_NOTHING:
            ret=(void*)0x01;
            break;
        case SECCOS_SELECT_RET_FCP:
            SECCOS_FCP *fcp=new SECCOS_FCP;
            fcp->fd=new unsigned char[0];
            fcp->dfname=new unsigned char[0];
              
            int pos=2; // skip TL
            len-=2; // without status
    
            while (pos<len) {
                switch (response[pos]) {
                    case 0x80:
                        fcp->reservedMem=(((unsigned short int)response[pos+2])<<8)+
                                         (((unsigned short int)response[pos+3])& 0xFF);
                        break;
                    case 0x82:
                        fcp->fdsize=response[pos+1];
                        fcp->fd=new unsigned char[fcp->fdsize];
                        memcpy(fcp->fd,response+pos+2,fcp->fdsize);
                        break;
                    case 0x83:
                        fcp->fileidsize=2;
                        memcpy(fcp->fileid,response+pos+2,fcp->fileidsize);
                        break;
                    case 0x84:
                        fcp->dfnamesize=response[pos+1];
                        fcp->dfname=new unsigned char[fcp->dfnamesize];
                        memcpy(fcp->dfname,response+pos+2,fcp->dfnamesize);
                        break;
                    // *** more data here
                }
    
                pos+=response[pos+1]+2;
            }
              
            ret=fcp;
            break;
    }
    
    return ret;
}

void* SECCOS_selectRoot(unsigned char returntype)
{
    // sometimes commanddata has to be empty
    unsigned char command[]=
    {
        SECCOS_CLA_STD,
        SECCOS_INS_SELECT_FILE,
        0x00,
        returntype,
        0x02,
        0x3F,
        0x00,
        0x00
    };
    unsigned short int len=300;
    unsigned char      *response=new unsigned char[len];
    
    unsigned short int status=CTAPI_performWithCard("selectRoot",(returntype==SECCOS_SELECT_RET_NOTHING)?7:8,command,&len,response);
    
    if (!CTAPI_isOK(status)) {
        command[0x04]=0x00;
        len=300;
        status=CTAPI_performWithCard("selectRoot(2)",(returntype==SECCOS_SELECT_RET_NOTHING)?4:5,command,&len,response);
    }
    
    void* ret;
    
    if (CTAPI_isOK(status)) {
        ret=extractSelectResult(len,response,returntype);
    } else {
        ret=NULL;
    }
    
    delete response;
    return ret;
}

void* SECCOS_selectSubFile(unsigned char returntype,unsigned short int fileid)
{
    unsigned char command[]=
    {
        SECCOS_CLA_STD,
        SECCOS_INS_SELECT_FILE,
        0x02,
        returntype,
        0x02,
        (fileid>>8)&0xFF,
        fileid&0xFF,
        0x00,
    };
    char               *descr=new char[32];
    unsigned short int len=300;
    unsigned char      *response=new unsigned char[len];
    
    sprintf(descr,"selectSubFile %04X",fileid);
    unsigned short int status=CTAPI_performWithCard(descr,(returntype==SECCOS_SELECT_RET_NOTHING)?7:8,command,&len,response);
    delete descr;
    
    void* ret;
    if (CTAPI_isOK(status))
        ret=extractSelectResult(len,response,returntype);
    else
        ret=NULL;
        
    delete response;
    return ret;
}

void* SECCOS_selectDF(unsigned char returntype,unsigned short int fileid)
{
    unsigned char command[]=
    {
        SECCOS_CLA_STD,
        SECCOS_INS_SELECT_FILE,
        0x01,
        returntype,
        0x02,
        (fileid>>8)&0xFF,
        fileid&0xFF,
        0x00,
    };
    char               *descr=new char[32];
    unsigned short int len=300;
    unsigned char      *response=new unsigned char[len];
    
    sprintf(descr,"selectDF %04X",fileid);
    unsigned short int status=CTAPI_performWithCard(descr,(returntype==SECCOS_SELECT_RET_NOTHING)?7:8,command,&len,response);
    delete descr;
    
    void* ret;
    if (CTAPI_isOK(status))
        ret=extractSelectResult(len,response,returntype);
    else
        ret=NULL;
        
    delete response;
    return ret;
}

void* SECCOS_selectFileByName(unsigned char returntype,unsigned char namesize,unsigned char *name)
{
    unsigned char *command=new unsigned char[6+namesize];
    
    command[0]=SECCOS_CLA_STD;
    command[1]=SECCOS_INS_SELECT_FILE;
    command[2]=0x04;
    command[3]=returntype;
    command[4]=namesize;
    memcpy(command+5,name,namesize);
    command[5+namesize]=0x00;
    
    char               *descr=new char[64];
    unsigned short int len=300;
    unsigned char      *response=new unsigned char[len];
    
    char *hex=bytes2hex(namesize,name);
    sprintf(descr,"selectFileByName %s",hex);
    unsigned short int status=CTAPI_performWithCard(descr,(returntype==SECCOS_SELECT_RET_NOTHING)?(5+namesize):(5+namesize+1),command,&len,response);
    
    delete command;
    delete descr;
    delete hex;

    void* ret;
    if (CTAPI_isOK(status))
        ret=extractSelectResult(len,response,returntype);
    else
        ret=NULL;

    delete response;
    return ret;
}

void* SECCOS_selectFileByPath(unsigned char returntype,unsigned char pathsize,unsigned char *path)
{
    unsigned char *command=new unsigned char[6+pathsize];
    
    command[0]=SECCOS_CLA_STD;
    command[1]=SECCOS_INS_SELECT_FILE;
    command[2]=0x08;
    command[3]=returntype;
    command[4]=pathsize;
    memcpy(command+5,path,pathsize);
    command[5+pathsize]=0x00;
    
    char               *descr=new char[64];
    unsigned short int len=300;
    unsigned char      *response=new unsigned char[len];
    
    char *hex=bytes2hex(pathsize,path);
    sprintf(descr,"selectFileByPath %s",hex);
    unsigned short int status=CTAPI_performWithCard(descr,(returntype==SECCOS_SELECT_RET_NOTHING)?(5+pathsize):(5+pathsize+1),command,&len,response);
    
    delete command;
    delete descr;
    delete hex;

    void* ret;
    if (CTAPI_isOK(status))
        ret=extractSelectResult(len,response,returntype);
    else
        ret=NULL;

    delete response;
    return ret;
}
