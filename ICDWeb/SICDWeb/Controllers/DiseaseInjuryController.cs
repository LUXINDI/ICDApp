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
    public class DiseaseInjuryController : ApiController
    {


        [HttpGet]
        public Page GetPage(int PageIndex, int PageSize)
        {
            return DBHelper.GetDataTable("SICD_DiseaseInjury", "*", "", "DiagnosisTerm", PageIndex, PageSize);
        }

        [HttpGet]
        public List<DiseaseInjurySelectResult> Get(string Term,int Flag)
        {
            try
            {
                if (Term == null) Term = "";
                  

                SICDDataContext sicd = new SICDDataContext(DBHelper.DBConnectionString);

                List<DiseaseInjurySelectResult> result = sicd.DiseaseInjurySelect(Term,Flag).ToList();

                return result;

            }
            catch (Exception ex)
            {
                return new List<DiseaseInjurySelectResult>();
            }
        }

        [HttpPost]
        public DiseaseInjurySelectSingleResult GetSingle(string Term)
        {
            try
            {
                SICDDataContext sicd = new SICDDataContext(DBHelper.DBConnectionString);

                DiseaseInjurySelectSingleResult result = sicd.DiseaseInjurySelectSingle(Term).SingleOrDefault();

                return result;

            }
            catch (Exception ex)
            {
                return null;
            }
        }

    }
}
