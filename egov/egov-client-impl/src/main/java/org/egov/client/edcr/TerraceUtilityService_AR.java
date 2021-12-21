package org.egov.client.edcr;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.common.entity.edcr.Plan;
import org.egov.edcr.feature.TerraceUtilityService;
import org.springframework.stereotype.Service;

@Service
public class TerraceUtilityService_AR extends TerraceUtilityService{

	private static final Logger LOG = Logger.getLogger(TerraceUtilityService_AR.class);
	
	@Override
	public Plan validate(Plan plan) {
		// TODO Auto-generated method stub
		return plan;
	}

	@Override
	public Plan process(Plan plan) {
		// TODO Auto-generated method stub
		return plan;
	}

	@Override
	public Map<String, Date> getAmendments() {
		// TODO Auto-generated method stub
		return super.getAmendments();
	
	}
}
