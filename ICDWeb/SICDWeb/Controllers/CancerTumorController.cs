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
    public class CancerTumorController : ApiController
    {

        [HttpGet]
        public Page GetPage(int PageIndex, int PageSize)
        {
            return DBHelper.GetDataTable("SICD_Cancer_Tumor", "*", "", "keyword", PageIndex, PageSize);
        }


        [HttpGet]
        public List<CancerTumorSelectResult> Get(string Term, int Flag)
        {
            try
            {
                if (Term == null) Term = "";
               
                SICDDataContext sicd = new SICDDataContext(DBHelper.DBConnectionString);

                List<CancerTumorSelectResult> result = sicd.CancerTumorSelect(Term, Flag).ToList();

                return result;

            }
            catch (Exception ex)
            {
                return new List<CancerTumorSelectResult>();
            }
        }

        [HttpPost]
        public CancerTumorSelectSingleResult GetSingle(string Term)
        {
            try
            {
                SICDDataContext sicd = new SICDDataContext(DBHelper.DBConnectionString);

                CancerTumorSelectSingleResult result = sicd.CancerTumorSelectSingle(Term).SingleOrDefault();

                return result;

            }
            catch (Exception ex)
            {
                return null;
            }
        }

    }
}
