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
    public class LandTransportController : ApiController
    {

        [HttpGet]
        public Page GetPage(int PageIndex, int PageSize)
        {
            return DBHelper.GetDataTable("SICD_LandTransport", "*", "", "Keyword1", PageIndex, PageSize);
        }


        [HttpGet]
        public List<LandTransportSelectResult> Get(string Term, int Flag)
        {
            try
            {
                if (Term == null) Term = "";

                if (Term == "") return new List<LandTransportSelectResult>();

                SICDDataContext sicd = new SICDDataContext(DBHelper.DBConnectionString);

                List<LandTransportSelectResult> result = sicd.LandTransportSelect(Term, Flag).ToList();

                return result;

            }
            catch (Exception ex)
            {
                return new List<LandTransportSelectResult>();
            }
        }

        [HttpPost]
        public List<LandTransportSelectSingleResult> GetSingle(string Term1)
        {
            try
            {
                SICDDataContext sicd = new SICDDataContext(DBHelper.DBConnectionString);

                List<LandTransportSelectSingleResult> result = sicd.LandTransportSelectSingle(Term1).ToList();

                return result;

            }
            catch (Exception ex)
            {
                return null;
            }
        }

    }
}
