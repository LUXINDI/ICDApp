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
    public class QuickSearch301Controller : ApiController
    {
        //
        // GET: /QuickSearch301/

        [HttpGet]
        public Page GetPage(int PageIndex, int PageSize)
        {
            return DBHelper.GetDataTable("tbldiagnosis_301", "*", "", "term", PageIndex, PageSize);
        }

        [HttpGet]
        public List<QuickSearch301SelectResult> Get (String Term,int Flag)
        {
            try
            {
                if (Term == null) Term = "";

                SICDDataContext sicd = new SICDDataContext(DBHelper.DBConnectionString);

                List<QuickSearch301SelectResult> result = sicd.QuickSearch301Select(Term, Flag).ToList();

                return result;
            }
            catch (Exception ex)
            {
                return new List<QuickSearch301SelectResult>();
            }
        }

        [HttpPost]
        public QuickSearch301SelectSingleResult GetSingle(string Term)
        {
            try
            {
                SICDDataContext sicd = new SICDDataContext(DBHelper.DBConnectionString);

                QuickSearch301SelectSingleResult result = sicd.QuickSearch301SelectSingle(Term).SingleOrDefault();

                return result;

            }
            catch (Exception ex)
            {
                return null;
            }
        }

    }
}
