package org.egov.client.edcr;

import static org.egov.client.constants.DxfFileConstants_AR.*;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.egov.client.constants.DxfFileConstants_AR;
import org.egov.common.entity.edcr.OccupancyTypeHelper;
import org.egov.common.entity.edcr.Plan;

public class Util_AR {

	protected static OccupancyTypeHelper getMostRestrictive(Plan pl) {
		
		Set<OccupancyTypeHelper> distinctOccupancyTypes=pl.getVirtualBuilding().getOccupancyTypes();
		
		Set<String> codes = new HashSet<>();
		Map<String, OccupancyTypeHelper> codesMap = new HashMap<>();
		for (OccupancyTypeHelper typeHelper : distinctOccupancyTypes) {

			if (typeHelper.getType() != null)
				codesMap.put(typeHelper.getType().getCode(), typeHelper);
			if (typeHelper.getSubtype() != null)
				codesMap.put(typeHelper.getSubtype().getCode(), typeHelper);
		}
		codes = codesMap.keySet();
		if (codes.contains(DxfFileConstants_AR.T1c))
			return codesMap.get(DxfFileConstants_AR.T1c);
		if (codes.contains(DxfFileConstants_AR.T1b))
			return codesMap.get(DxfFileConstants_AR.T1b);
		if (codes.contains(DxfFileConstants_AR.T1a))
			return codesMap.get(DxfFileConstants_AR.T1a);
		if (codes.contains(DxfFileConstants_AR.T))
			return codesMap.get(DxfFileConstants_AR.T);
		if (codes.contains(DxfFileConstants_AR.P3c))
			return codesMap.get(DxfFileConstants_AR.P3c);
		if (codes.contains(DxfFileConstants_AR.P3b))
			return codesMap.get(DxfFileConstants_AR.P3b);
		if (codes.contains(DxfFileConstants_AR.P3a))
			return codesMap.get(DxfFileConstants_AR.P3a);
		if (codes.contains(DxfFileConstants_AR.P2d))
			return codesMap.get(DxfFileConstants_AR.P2d);
		if (codes.contains(DxfFileConstants_AR.P2c))
			return codesMap.get(DxfFileConstants_AR.P2c);
		if (codes.contains(DxfFileConstants_AR.P2b))
			return codesMap.get(DxfFileConstants_AR.P2b);
		if (codes.contains(DxfFileConstants_AR.P2a))
			return codesMap.get(DxfFileConstants_AR.P2a);
		if (codes.contains(DxfFileConstants_AR.P1g))
			return codesMap.get(DxfFileConstants_AR.P1g);
		if (codes.contains(DxfFileConstants_AR.P1f))
			return codesMap.get(DxfFileConstants_AR.P1f);
		if (codes.contains(DxfFileConstants_AR.P1e))
			return codesMap.get(DxfFileConstants_AR.P1e);
		if (codes.contains(DxfFileConstants_AR.P1d))
			return codesMap.get(DxfFileConstants_AR.P1d);
		if (codes.contains(DxfFileConstants_AR.P1c))
			return codesMap.get(DxfFileConstants_AR.P1c);
		if (codes.contains(DxfFileConstants_AR.P1b))
			return codesMap.get(DxfFileConstants_AR.P1b);
		if (codes.contains(DxfFileConstants_AR.P1a))
			return codesMap.get(DxfFileConstants_AR.P1a);
		if (codes.contains(DxfFileConstants_AR.P))
			return codesMap.get(DxfFileConstants_AR.P);
		if (codes.contains(DxfFileConstants_AR.G3c))
			return codesMap.get(DxfFileConstants_AR.G3c);
		if (codes.contains(DxfFileConstants_AR.G3b))
			return codesMap.get(DxfFileConstants_AR.G3b);
		if (codes.contains(DxfFileConstants_AR.G3a))
			return codesMap.get(DxfFileConstants_AR.G3a);
		if (codes.contains(DxfFileConstants_AR.G2a))
			return codesMap.get(DxfFileConstants_AR.G2a);
		if (codes.contains(DxfFileConstants_AR.G1a))
			return codesMap.get(DxfFileConstants_AR.G1a);
		if (codes.contains(DxfFileConstants_AR.G))
			return codesMap.get(DxfFileConstants_AR.G);
		if (codes.contains(DxfFileConstants_AR.I1b))
			return codesMap.get(DxfFileConstants_AR.I1b);
		if (codes.contains(DxfFileConstants_AR.I1a))
			return codesMap.get(DxfFileConstants_AR.I1a);
		if (codes.contains(DxfFileConstants_AR.I))
			return codesMap.get(DxfFileConstants_AR.I);
		if (codes.contains(DxfFileConstants_AR.C6a))
			return codesMap.get(DxfFileConstants_AR.C6a);
		if (codes.contains(DxfFileConstants_AR.C5a))
			return codesMap.get(DxfFileConstants_AR.C5a);
		if (codes.contains(DxfFileConstants_AR.C4a))
			return codesMap.get(DxfFileConstants_AR.C4a);
		if (codes.contains(DxfFileConstants_AR.C3a))
			return codesMap.get(DxfFileConstants_AR.C3a);
		if (codes.contains(DxfFileConstants_AR.C2a))
			return codesMap.get(DxfFileConstants_AR.C2a);
		if (codes.contains(DxfFileConstants_AR.C1c))
			return codesMap.get(DxfFileConstants_AR.C1c);
		if (codes.contains(DxfFileConstants_AR.C1b))
			return codesMap.get(DxfFileConstants_AR.C1b);
		if (codes.contains(DxfFileConstants_AR.C1a))
			return codesMap.get(DxfFileConstants_AR.C1a);
		if (codes.contains(DxfFileConstants_AR.C))
			return codesMap.get(DxfFileConstants_AR.C);
		if (codes.contains(DxfFileConstants_AR.R2a))
			return codesMap.get(DxfFileConstants_AR.R2a);
		if (codes.contains(DxfFileConstants_AR.R1c))
			return codesMap.get(DxfFileConstants_AR.R1c);
		if (codes.contains(DxfFileConstants_AR.R1b))
			return codesMap.get(DxfFileConstants_AR.R1b);
		if (codes.contains(DxfFileConstants_AR.R1a))
			return codesMap.get(DxfFileConstants_AR.R1a);
		if (codes.contains(DxfFileConstants_AR.R))
			return codesMap.get(DxfFileConstants_AR.R);
		else
			return null;
	

	}

}
