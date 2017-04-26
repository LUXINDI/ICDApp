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
    public class DrugChemicalController : ApiController
    {

        [HttpGet]
        public Page GetPage(int PageIndex, int PageSize)
        {
            return DBHelper.GetDataTable("SICD_Drug_Chemical", "*", "", "DrugOrChemicalProduct", PageIndex, PageSize);
        }

        [HttpGet]
        public List<DrugChemicalSelectResult> Get(string Term, int Flag)
        {
            try
            {

                if (Term == null) Term = "";

                SICDDataContext sicd = new SICDDataContext(DBHelper.DBConnectionString);

                List<DrugChemicalSelectResult> result = sicd.DrugChemicalSelect(Term, Flag).ToList();

                return result;

            }
            catch (Exception ex)
            {
                return new List<DrugChemicalSelectResult>();
            }
        }

        [HttpPost]
        public DrugChemicalSelectSingleResult GetSingle(string Term)
        {
            try
            {
                SICDDataContext sicd = new SICDDataContext(DBHelper.DBConnectionString);

                DrugChemicalSelectSingleResult result = sicd.DrugChemicalSelectSingle(Term).SingleOrDefault();

                return result;

            }
            catch (Exception ex)
            {
                return null;
            }
        }

    }
}
