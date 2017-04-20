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
    public class QuickSearchBjController : ApiController
    {
        //
        // GET: /QuickSearchBj/

        [HttpGet]
        public List<QuickSearchBjSelectResult> Get(String Term, int Flag)
        {
            try
            {
                if (Term == null) Term = "";

                SICDDataContext sicd = new SICDDataContext(DBHelper.DBConnectionString);

                List<QuickSearchBjSelectResult> result = sicd.QuickSearchBjSelect(Term, Flag).ToList();

                return result;
            }
            catch (Exception ex)
            {
                return new List<QuickSearchBjSelectResult>();
            }
        }

        [HttpPost]
        public QuickSearchBjSelectSingleResult GetSingle(string Term)
        {
            try
            {
                SICDDataContext sicd = new SICDDataContext(DBHelper.DBConnectionString);

                QuickSearchBjSelectSingleResult result = sicd.QuickSearchBjSelectSingle(Term).SingleOrDefault();

                return result;

            }
            catch (Exception ex)
            {
                return null;
            }
        }

    }

}
