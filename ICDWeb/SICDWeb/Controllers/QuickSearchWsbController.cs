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
    public class QuickSearchWsbController : ApiController
    {
        //
        // GET: /QuickSearchWsb/

        [HttpGet]
        public Page GetPage(int PageIndex, int PageSize)
        {
            return DBHelper.GetDataTable("tbldiagnosis_wsb", "*", "", "term", PageIndex, PageSize);
        }

        [HttpGet]
        public List<QuickSearchWsbSelectResult> Get(String Term, int Flag)
        {
            try
            {
                if (Term == null) Term = "";

                SICDDataContext sicd = new SICDDataContext(DBHelper.DBConnectionString);

                List<QuickSearchWsbSelectResult> result = sicd.QuickSearchWsbSelect(Term, Flag).ToList();

                return result;
            }
            catch (Exception ex)
            {
                return new List<QuickSearchWsbSelectResult>();
            }
        }

        [HttpPost]
        public QuickSearchWsbSelectSingleResult GetSingle(string Term)
        {
            try
            {
                SICDDataContext sicd = new SICDDataContext(DBHelper.DBConnectionString);

                QuickSearchWsbSelectSingleResult result = sicd.QuickSearchWsbSelectSingle(Term).SingleOrDefault();

                return result;

            }
            catch (Exception ex)
            {
                return null;
            }
        }

    }

}
