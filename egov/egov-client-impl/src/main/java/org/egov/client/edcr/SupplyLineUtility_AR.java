package org.egov.client.edcr;

import static org.egov.edcr.utility.DcrConstants.OBJECTNOTDEFINED;
import static org.egov.edcr.utility.DcrConstants.PLOT_AREA;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.egov.common.entity.edcr.Measurement;
import org.egov.common.entity.edcr.Plan;
import org.egov.common.entity.edcr.Result;
import org.egov.common.entity.edcr.ScrutinyDetail;
import org.egov.common.entity.edcr.SupplyLine;
import org.egov.common.entity.edcr.Utility;
import org.egov.edcr.feature.SupplyLineUtility;
import org.egov.edcr.utility.DcrConstants;
import org.egov.infra.utils.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class SupplyLineUtility_AR extends SupplyLineUtility {

	

	@Override
	public Plan validate(Plan plan) {
		// TODO Auto-generated method stub

		return plan;
	}

	@Override
	public Plan process(Plan plan) {
		

		return plan;
	}

	

}
