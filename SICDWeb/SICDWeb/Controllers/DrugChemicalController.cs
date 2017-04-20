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
    public class ExternalCauseController : ApiController
    {



        [HttpGet]
        public Page GetPage(int PageIndex, int PageSize)
        {
            return DBHelper.GetDataTable("SICD_ExternalCauses", "*", "", "Cause", PageIndex, PageSize);
        }


        [HttpGet]
        public List<ExternalCauseSelectResult> Get(string Term, int Flag)
        {
            try
            {
                if (Term == null) Term = "";
            
                SICDDataContext sicd = new SICDDataContext(DBHelper.DBConnectionString);

                List<ExternalCauseSelectResult> result = sicd.ExternalCauseSelect(Term, Flag).ToList();

                return result;

            }
            catch (Exception ex)
            {
                return new List<ExternalCauseSelectResult>();
            }
        }

        [HttpPost]
        public ExternalInjurySelectSingleResult GetSingle(string Term)
        {
            try
            {
                SICDDataContext sicd = new SICDDataContext(DBHelper.DBConnectionString);

                ExternalInjurySelectSingleResult result = sicd.ExternalInjurySelectSingle(Term).SingleOrDefault();

                return result;

            }
            catch (Exception ex)
            {
                return null;
            }
        }

    }
}
