package org.egov.client.edcr;

import org.egov.common.entity.edcr.Plan;
import org.egov.edcr.feature.SegregatedToilet;
import org.springframework.stereotype.Service;

@Service
public class SegregatedToilet_AR extends SegregatedToilet{

	@Override
	public Plan process(Plan pl) {
		return pl;
	}

	@Override
	public Plan validate(Plan pl) {
		return pl;
	}
}
