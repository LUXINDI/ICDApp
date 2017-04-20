using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Web;

namespace SICDWeb.Utils
{

    public class Page
    {


        public Page()
        {
            DataTable = new DataTable();
            PageCount = 0;
            RecordCount = 0;
        }

        public Page(DataTable dt, int pc, int rc)
        {
            DataTable = dt;
            PageCount = pc;
            RecordCount = rc;
        }
        public int RecordCount { get; set; }
        public int PageCount { get; set; }
        public DataTable DataTable { get; set; }
    }

    public class DBHelper
    {
        public static string DBConnectionString
        {
            get
            {
                return ConfigurationManager.ConnectionStrings["SICDConnectionString"].ConnectionString
                    ;
            }
        }


        public static Page GetDataTable(string table,
          string listfield, string wherecause, string orderfield,
  int pageid, int pagesize)
        {
            int pagecount = 0;
            Page page = new Page();
            // string conn = System.Configuration.ConfigurationManager.ConnectionStrings["PheiSDAL.Properties.Settings.PheiPublishingConnectionString"].ConnectionString;
            SqlConnection sqlconn = new SqlConnection(DBConnectionString);
            SqlCommand cmd = new SqlCommand();
            // 设置sql连接
            cmd.Connection = sqlconn;
            // 如果执行语句
            cmd.CommandText = "UTIL_PAGER";
            // 指定执行语句为存储过程
            cmd.CommandType = CommandType.StoredProcedure;
            IDataParameter[] para = {
                new SqlParameter("@TableName",SqlDbType.VarChar,255) ,
                new SqlParameter("@Fields", SqlDbType.VarChar,8000) ,          
                new SqlParameter("@OrderField", SqlDbType.VarChar,255) ,
                new SqlParameter("@sqlWhere", SqlDbType.VarChar,8000) ,
                new SqlParameter("@PageSize", SqlDbType.Int) ,
                new SqlParameter("@PageIndex", SqlDbType.Int) ,
                new SqlParameter("@TotalPage", SqlDbType.Int) ,    
             };
            para[0].Value = table;
            para[1].Value = listfield;
            para[2].Value = orderfield;
            para[3].Value = wherecause;
            para[4].Value = pagesize;
            para[5].Value = pageid;
            para[6].Value = pagecount;
            para[6].Direction = ParameterDirection.Output;
            cmd.Parameters.Add(para[0]);
            cmd.Parameters.Add(para[1]);
            cmd.Parameters.Add(para[2]);
            cmd.Parameters.Add(para[3]);
            cmd.Parameters.Add(para[4]);
            cmd.Parameters.Add(para[5]);
            cmd.Parameters.Add(para[6]);

            cmd.Parameters.Add(new SqlParameter("@return", SqlDbType.Int));
            cmd.Parameters["@return"].Direction = ParameterDirection.ReturnValue;
            //  cmd.ExecuteNonQuery();

            SqlDataAdapter dp = new SqlDataAdapter(cmd);
            DataSet ds = new DataSet();
            dp.Fill(ds);
            if (dp != null && ds != null)
            {
                try
                {
                    DataTable dt = ds.Tables[0];

                    pagecount = Convert.ToInt32(cmd.Parameters[6].Value);

                    int rcount = (int)cmd.Parameters["@return"].Value;
                    page.PageCount = pagecount;
                    page.RecordCount = rcount;

                    dp = null;
                    sqlconn.Close();
                    sqlconn.Dispose();
                    page.DataTable = dt;
                    return page;
                }
                catch (Exception)
                {
                    dp = null;
                    sqlconn.Close();
                    sqlconn.Dispose();
                    return null;
                }
            }
            dp = null;
            sqlconn.Close();
            sqlconn.Dispose();
            return null;
        }



    }
}