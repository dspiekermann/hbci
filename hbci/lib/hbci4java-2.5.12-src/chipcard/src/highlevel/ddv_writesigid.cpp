
/*  $Id: ddv_writesigid.cpp 62 2008-10-22 17:03:26Z kleiner $

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

#include "ddvcard.h"
#include "seccos.h"

bool DDV_writeSigId(unsigned short int sigid)
{
    unsigned char buffer[]={(sigid>>8)&0xFF,sigid&0xFF};
    return SECCOS_writeRecordBySFI(DDV_EF_SEQ,1,buffer,2);
}
