package kd.idp.psidpapp.opt.ticket.constant;

public class FieldName {

	//操作票表字段名
	public static String FIELD_OPT_OPT_TICKET = "ID,TICKETNUMBER,OPERTASK,FASHION,OPENTICKETTIME,DRAFTERID,DRAFTER,AUDITORID,AUDITOR,RECVTICKETERID,RECVTICKETER,RECVTIME,TICKETSTATUS,TICKETTYPE,TICKETOPERSTARTTIME,TICKETOPERENTTIME,EXT1,EXT2,EXT3,EXT4,EXT5,CLASS,SORT,DATASTATUSID,CREATEUSERID,CREATETIME,UPDATEUSERID,UPDATETIME,REMARK";
	//操作项表字段名
	public static String FIELD_OPT_OPT_ITEM = "ID,OPERUNITID,OPERUNIT,RECVUNITID,OPERITEM,SENDLTIME,SENDLUSERID,SENDLUSER,RECVLUSERID,RECVLUSER,OVERLTIME,CALLLTIME,CALLLUSERID,CALLLUSER,GETLUSERID,GETLUSER,JHRLUSERID,JHRLUSER,OPERLSTATUS,TICKETID,EXT1,EXT2,EXT3,EXT4,EXT5,CLASS,SORT,DATASTATUSID,CREATEUSERID,CREATETIME,UPDATEUSERID,UPDATETIME,REMARK";
	//操作令表字段名
	public static String FIELD_OPT_OPT_ORDER = "ID,NAME,OPERTASK,TICKETID,ITEMSORT,OPERUNIT,OPERUNITID,RECVUNITID,ORDERSTATUS,ORDERCONTENT,SENDLTIME,SENDLUSERID,SENDLUSER,RECVLUSERID,RECVLUSER,OVERLTIME,CALLLTIME,GETLUSERID,GETLUSER,JHRLUSERID,JHRLUSER,FWFXJG,EXT1,EXT2,EXT3,EXT4,EXT5,CLASS,SORT,DATASTATUSID,CREATEUSERID,CREATETIME,UPDATEUSERID,UPDATETIME,REMARK,CALLLUSER,CALLLUSERID";
	//受票表字段名
	public static String FIELD_OPT_OPT_RECVTICEKT = "ID,OPERUNITID,OPERUNIT,RECVUNITID,RECVTICKETERID,RECVTICKETER,RECVTIME,RECVTICKETSTATUS,TICKETID,EXT1,EXT2,EXT3,EXT4,EXT5,CLASS,SORT,DATASTATUSID,CREATEUSERID,CREATETIME,UPDATEUSERID,UPDATETIME,REMARK";
	public static String FIELD_OPT_TT = "OO,PP";
}
