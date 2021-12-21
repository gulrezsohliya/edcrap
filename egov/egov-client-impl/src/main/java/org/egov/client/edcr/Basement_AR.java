package org.egov.client.edcr;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.egov.common.entity.edcr.Block;
import org.egov.common.entity.edcr.Floor;
import org.egov.common.entity.edcr.Plan;
import org.egov.edcr.feature.Basement;
import org.egov.edcr.utility.DcrConstants;
import org.springframework.stereotype.Service;

@Service
public class Basement_AR extends Basement {

	private static final Logger LOG = Logger.getLogger(Basement_AR.class);

	@Override
	public Plan validate(Plan plan) {

		return plan;
	}

	@Override
	public Plan process(Plan plan) {
		validate(plan);
		return plan;
	}
}
