package org.egov.client.edcr;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.common.entity.edcr.Plan;
import org.egov.common.entity.edcr.Result;
import org.egov.common.entity.edcr.ScrutinyDetail;
import org.egov.edcr.feature.SepticTank;
import org.egov.edcr.utility.DcrConstants;
import org.springframework.stereotype.Service;

@Service
public class SepticTank_AR extends SepticTank{

	private static final Logger LOG = Logger.getLogger(SepticTank_AR.class);
	
	private static final String DECLARED = "Declared";
	private static final String RULE_4_6_i_2_e_8_25 = "4, 6(i) 2(e), 8 & 25";
	
	@Override
	public Plan validate(Plan plan) {
	
		return plan;
	}

	@Override
	public Plan process(Plan plan) {
		return plan;
	}

	
}
