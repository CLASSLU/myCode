package kd.idp.common.database;
/**
 * 2007-02-09 LS
 * <p>Title: 数据库操作异常信息类</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2006</p>
 *
 * <p>Company: </p>
 *
 * @author not attributable
 * @version 1.0
 */
public class DBException {

    int errorCode = 0;
    String errorMesage ="";

    public DBException() {
    }




    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMesage() {
        return errorMesage;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setErrorMesage(String errorMesage) {
        this.errorMesage = errorMesage;
    }

    public static void main(String[] args) {
        DBException databaseexceptionmessage = new
                DBException();
    }

}
