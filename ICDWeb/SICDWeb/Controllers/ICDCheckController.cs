using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using SICDDAL;
using SICDWeb.Utils;

namespace SICDWeb.Controllers
{
    public class ICDCheckController : ApiController
    {
        //
        // GET: /ICDCheck/

        [HttpGet]
        //核对编码的名字、类型、页数
        public ICDCheck_codenameResult GetCodeinfo(string icd_code)
        {
            try
            {
                SICDDataContext sicd = new SICDDataContext(DBHelper.DBConnectionString);

                ICDCheck_codenameResult result = sicd.ICDCheck_codename(icd_code).SingleOrDefault();

                return result;

            }
            catch (Exception ex)
            {
                return null;
            }
        }

        [HttpGet]
        public List<ICDCheck_includeResult> GetInclude(string icd_code_include)
        {
            try
            {

                SICDDataContext sicd = new SICDDataContext(DBHelper.DBConnectionString);

                List<ICDCheck_includeResult> result = sicd.ICDCheck_include(icd_code_include).ToList();

                return result;

            }
            catch (Exception ex)
            {
                return null;
            }
        }

        [HttpGet]
        public List<ICDCheck_excludeResult> GetExclude(string icd_code_exclude)
        {
            try
            {

                SICDDataContext sicd = new SICDDataContext(DBHelper.DBConnectionString);

                List<ICDCheck_excludeResult> result = sicd.ICDCheck_exclude(icd_code_exclude).ToList();

                return result;

            }
            catch (Exception ex)
            {
                return null;
            }
        }

    }
}
