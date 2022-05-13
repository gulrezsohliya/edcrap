
package org.egov.client.edcr;

import static org.egov.edcr.utility.DcrConstants.HEIGHTNOTDEFINED;
import static org.egov.edcr.utility.DcrConstants.OBJECTNOTDEFINED;
import static org.egov.edcr.utility.DcrConstants.WRONGHEIGHTDEFINED;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import org.egov.common.entity.edcr.Block;
import org.egov.common.entity.edcr.Plan;
import org.egov.common.entity.edcr.SetBack;
import org.egov.edcr.feature.SetBackService;
import org.egov.edcr.utility.DcrConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetBackService_AR extends SetBackService {
	private static final Logger LOG = Logger.getLogger(SetBackService_AR.class);
    @Autowired
    private FrontYardService_AR frontYardService;

    @Autowired
    private SideYardService_AR sideYardService;

    @Autowired
    private RearYardService_AR rearYardService;

    @Override
    public Plan validate(Plan pl) {
        HashMap<String, String> errors = new HashMap<>();
       
        if (errors.size() > 0)
            pl.addErrors(errors);

        return pl;
    }

    @Override
    public Plan process(Plan pl) {
    	try {
            validate(pl);
    			frontYardService.processFrontYard(pl);
    			rearYardService.processRearYard(pl);
    			sideYardService.processSideYard(pl);
		} catch (Exception e) {
			// TODO: handle exception
		}


        return pl;
    }

    @Override
    public Map<String, Date> getAmendments() {
        return new LinkedHashMap<>();
    }
}
