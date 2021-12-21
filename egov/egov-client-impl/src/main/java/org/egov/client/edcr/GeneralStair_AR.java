package org.egov.client.edcr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.egov.common.entity.edcr.Block;
import org.egov.common.entity.edcr.Floor;
import org.egov.common.entity.edcr.Plan;
import org.egov.edcr.feature.GeneralStair;
import org.springframework.stereotype.Service;

@Service
public class GeneralStair_AR extends GeneralStair{

	private static final Logger LOG = Logger.getLogger(GeneralStair_AR.class);
	
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
