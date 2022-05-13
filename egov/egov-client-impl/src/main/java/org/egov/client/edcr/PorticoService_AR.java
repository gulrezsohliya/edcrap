package org.egov.client.edcr;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.common.entity.dcr.helper.OccupancyHelperDetail;
import org.egov.common.entity.edcr.Block;
import org.egov.common.entity.edcr.Plan;
import org.egov.common.entity.edcr.Portico;
import org.egov.common.entity.edcr.Result;
import org.egov.common.entity.edcr.ScrutinyDetail;
import org.egov.edcr.feature.PorticoService;
import org.egov.edcr.utility.DcrConstants;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PorticoService_AR extends PorticoService {

	private static final Logger LOG = Logger.getLogger(PorticoService_AR.class);

	private static final String OCCUPANCY = "Occupancy/Sub-Occupancy";

	private static final String RULE_RESIDENTIAL = "13.6.1.1-Note XIV";
	private static final String RULE_RESIDENTIAL_PLOTTED_HOUSING = "13.6.1.1-Note XIV";
	private static final String SUBRULE_PORTICO = "PORTICO";
	private static final String SUBRULE_PORTICO_MAX_LENGTHDESCRIPTION = "Maximum Portico length for portico %s ";
	public static final String PORTICO_DISTANCETO_EXTERIORWALL = "Block %s Portico %s Portico distance to exteriorwall";
	public static final String PORTICO_AREA = "Block %s Portico %s Portico area";

	private static final String RULE_DESC_LENGTH = "Length of portico %s ";
	private static final String RULE_DESC_MAXIMUM_AREA = "Maximum area for portico %s ";

	private static final String ERROR_ROW_HOUSING = "Portico not allowed in %s";

	private static final BigDecimal MAX_PERMISSIBLE_AREA = BigDecimal.valueOf(17);

	@Override
	public Plan validate(Plan plan) {
//		HashMap<String, String> errors = new HashMap<>();
//
//		for (Block block : plan.getBlocks()) {
//
//			OccupancyHelperDetail occupancyType = block.getBuilding().getMostRestrictiveFarHelper() != null
//					? block.getBuilding().getMostRestrictiveFarHelper().getType() != null
//							&& block.getBuilding().getMostRestrictiveFarHelper().getType().getCode() != null
//									? block.getBuilding().getMostRestrictiveFarHelper().getType()
//									: null
//					: null;
//
//			OccupancyHelperDetail subOccupancyType = block.getBuilding().getMostRestrictiveFarHelper() != null
//					? block.getBuilding().getMostRestrictiveFarHelper().getSubtype() != null
//							&& block.getBuilding().getMostRestrictiveFarHelper().getSubtype().getCode() != null
//									? block.getBuilding().getMostRestrictiveFarHelper().getSubtype()
//									: null
//					: null;
//			for (Portico portico : block.getPorticos()) {
//				scrutinyDetail = new ScrutinyDetail();
//				scrutinyDetail.addColumnHeading(1, RULE_NO);
//				scrutinyDetail.addColumnHeading(2, DESCRIPTION);
//				scrutinyDetail.addColumnHeading(3, OCCUPANCY);
//				scrutinyDetail.addColumnHeading(4, REQUIRED);
//				scrutinyDetail.addColumnHeading(5, PROVIDED);
//				scrutinyDetail.addColumnHeading(6, STATUS);
//				scrutinyDetail.setKey("Block_" + block.getNumber() + "_" + "Portico");
//
//				String actual = "", expected = "", occupancy = "", ruleNo = "", ruleDesc = "";
//				boolean isAccepted = false;
//				if (occupancyType != null && subOccupancyType != null) {
//					occupancy = subOccupancyType.getName();
//					if (occupancyType.getCode().equals(OccupancyType.OCCUPANCY_RESIDENTIAL.getOccupancyType())) {
//						ruleNo = RULE_RESIDENTIAL;
//						if (subOccupancyType.getCode().equals(
//								OccupancyType.OCCUPANCY_RESIDENTIAL_PLOTTED_HOUSING_ROW_HOUSES.getOccupancyType())) {
//							if (portico.getArea() != null && portico.getArea().compareTo(BigDecimal.ZERO) > 0) {
//								errors.put(String.format(ERROR_ROW_HOUSING, subOccupancyType.getName()),
//										String.format(ERROR_ROW_HOUSING, subOccupancyType.getName()));
//								plan.addErrors(errors);
//
//							}
//						} else if (subOccupancyType.getCode().equals(
//								OccupancyType.OCCUPANCY_RESIDENTIAL_PLOTTED_HOUSING_DETACHED_HOUSES.getOccupancyType())
//								|| subOccupancyType.getCode()
//										.equals(OccupancyType.OCCUPANCY_RESIDENTIAL_PLOTTED_HOUSING_SEMI_DETACHED_HOUSES
//												.getOccupancyType())) {
//							if (portico.getArea() != null) {
//								ruleDesc = String.format(RULE_DESC_MAXIMUM_AREA, portico.getName());
//								expected = "<= " + MAX_PERMISSIBLE_AREA + " " + DcrConstants.SQMTRS;
//								actual = portico.getArea().setScale(2, DcrConstants.ROUNDMODE_MEASUREMENTS.DOWN) + " "
//										+ DcrConstants.SQMTRS;
//								if (portico.getArea().compareTo(MAX_PERMISSIBLE_AREA) <= 0) {
//									isAccepted = true;
//								} else {
//									isAccepted = false;
//								}
//								setReportOutputDetails(plan, ruleNo, ruleDesc, occupancy, expected, actual,
//										isAccepted ? Result.Accepted.getResultVal()
//												: Result.Not_Accepted.getResultVal(),
//										scrutinyDetail);
//
//							} else {
//								errors.put(String.format(PORTICO_AREA, block.getNumber(), portico.getName()),
//										edcrMessageSource.getMessage(
//												DcrConstants.OBJECTNOTDEFINED, new String[] { String
//														.format(PORTICO_AREA, block.getNumber(), portico.getName()) },
//												LocaleContextHolder.getLocale()));
//								plan.addErrors(errors);
//							}
//						} else {
//							/*
//							 * setReportOutputDetails(plan, ruleNo, ruleDesc, occupancy, expected, actual,
//							 * status, scrutinyDetail);
//							 */
//						}
//					}
//				}
//			}
//		}
		return plan;
	}

	@Override
	public Plan process(Plan plan) {
		if (plan != null)
			try {
				validate(plan);
			} catch (Exception e) {
				e.printStackTrace();
			}

		return plan;
	}

	private void setReportOutputDetails(Plan pl, String ruleNo, String ruleDesc, String occupancy, String expected,
			String actual, String status, ScrutinyDetail scrutinyDetail) {
		Map<String, String> details = new HashMap<>();
		details.put(RULE_NO, ruleNo);
		details.put(DESCRIPTION, ruleDesc);
		details.put(OCCUPANCY, occupancy);
		details.put(REQUIRED, expected);
		details.put(PROVIDED, actual);
		details.put(STATUS, status);
		scrutinyDetail.getDetail().add(details);
		pl.getReportOutput().getScrutinyDetails().add(scrutinyDetail);
	}

	@Override
	public Map<String, Date> getAmendments() {
		return new LinkedHashMap<>();
	}

}
