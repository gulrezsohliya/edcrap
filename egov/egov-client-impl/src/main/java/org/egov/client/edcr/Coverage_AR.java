package org.egov.client.edcr;

import static org.egov.edcr.constants.DxfFileConstants.A;
import static org.egov.edcr.constants.DxfFileConstants.G;
import static org.egov.edcr.constants.DxfFileConstants.B;
import static org.egov.edcr.constants.DxfFileConstants.C;

import static org.egov.edcr.constants.DxfFileConstants.A2;

import static org.egov.edcr.constants.DxfFileConstants.A_FH;
import static org.egov.edcr.constants.DxfFileConstants.A_R;
import static org.egov.edcr.constants.DxfFileConstants.A_SA;
import static org.egov.edcr.constants.DxfFileConstants.A_AF;
import static org.egov.edcr.constants.DxfFileConstants.A_HE;

import static org.egov.edcr.constants.DxfFileConstants.B2;
import static org.egov.edcr.constants.DxfFileConstants.B_PS;
import static org.egov.edcr.constants.DxfFileConstants.D_A;
import static org.egov.edcr.constants.DxfFileConstants.D_B;
import static org.egov.edcr.constants.DxfFileConstants.D_C;
import static org.egov.edcr.constants.DxfFileConstants.E_CLG;
import static org.egov.edcr.constants.DxfFileConstants.E_EARC;
import static org.egov.edcr.constants.DxfFileConstants.E_NS;
import static org.egov.edcr.constants.DxfFileConstants.E_PS;
import static org.egov.edcr.constants.DxfFileConstants.E_SACA;
import static org.egov.edcr.constants.DxfFileConstants.E_SFDAP;
import static org.egov.edcr.constants.DxfFileConstants.E_SFMC;
import static org.egov.edcr.constants.DxfFileConstants.F;
import static org.egov.edcr.constants.DxfFileConstants.F_H;
import static org.egov.edcr.constants.DxfFileConstants.F_CB;

import static org.egov.edcr.constants.DxfFileConstants.H_PP;
import static org.egov.edcr.constants.DxfFileConstants.M_DFPAB;
import static org.egov.edcr.constants.DxfFileConstants.M_HOTHC;
import static org.egov.edcr.constants.DxfFileConstants.M_NAPI;
import static org.egov.edcr.constants.DxfFileConstants.M_OHF;
import static org.egov.edcr.constants.DxfFileConstants.M_VH;
import static org.egov.edcr.constants.DxfFileConstants.S_BH;
import static org.egov.edcr.constants.DxfFileConstants.S_CA;
import static org.egov.edcr.constants.DxfFileConstants.S_CRC;
import static org.egov.edcr.constants.DxfFileConstants.S_ECFG;
import static org.egov.edcr.constants.DxfFileConstants.S_ICC;
import static org.egov.edcr.constants.DxfFileConstants.S_MCH;
import static org.egov.edcr.constants.DxfFileConstants.S_SAS;
import static org.egov.edcr.constants.DxfFileConstants.S_SC;
import static org.egov.edcr.constants.DxfFileConstants.F_RT;

//from  DxfFileConstants_AR
import static org.egov.client.constants.DxfFileConstants_AR.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.egov.common.entity.edcr.Block;
import org.egov.common.entity.edcr.Measurement;
import org.egov.common.entity.edcr.OccupancyType;
import org.egov.common.entity.edcr.OccupancyTypeHelper;
import org.egov.common.entity.edcr.Plan;
import org.egov.common.entity.edcr.Result;
import org.egov.common.entity.edcr.ScrutinyDetail;
import org.egov.edcr.feature.Coverage;
import org.egov.edcr.utility.DcrConstants;
import org.egov.infra.utils.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class Coverage_AR extends Coverage {
	// private static final String OCCUPANCY2 = "OCCUPANCY";

	private static final Logger LOG = Logger.getLogger(Coverage_AR.class);

	// private static final String RULE_NAME_KEY = "coverage.rulename";
	private static final String RULE_DESCRIPTION_KEY = "coverage.description";
	private static final String RULE_EXPECTED_KEY = "coverage.expected";
	private static final String RULE_ACTUAL_KEY = "coverage.actual";
	// private static final BigDecimal ThirtyFive = BigDecimal.valueOf(35);
	private static final BigDecimal Forty = BigDecimal.valueOf(40);
	/*
	 * private static final BigDecimal FortyFive = BigDecimal.valueOf(45); private
	 * static final BigDecimal Sixty = BigDecimal.valueOf(60); private static final
	 * BigDecimal SixtyFive = BigDecimal.valueOf(65); private static final
	 * BigDecimal Seventy = BigDecimal.valueOf(70); private static final BigDecimal
	 * SeventyFive = BigDecimal.valueOf(75); private static final BigDecimal Eighty
	 * = BigDecimal.valueOf(80);
	 */
	public static final String RULE_38 = "38";
	private static final BigDecimal ROAD_WIDTH_TWELVE_POINTTWO = BigDecimal.valueOf(12.2);
	private static final BigDecimal ROAD_WIDTH_THIRTY_POINTFIVE = BigDecimal.valueOf(30.5);

	private static final BigDecimal PLOT_AREA_48 = BigDecimal.valueOf(48);
	private static final BigDecimal PLOT_AREA_60 = BigDecimal.valueOf(60);
	private static final BigDecimal PLOT_AREA_100 = BigDecimal.valueOf(100);
	private static final BigDecimal PLOT_AREA_250 = BigDecimal.valueOf(250);
	private static final BigDecimal PLOT_AREA_500 = BigDecimal.valueOf(500);
	private static final BigDecimal PLOT_AREA_1000 = BigDecimal.valueOf(1000);
	private static final BigDecimal PLOT_AREA_1500 = BigDecimal.valueOf(1500);
	private static final BigDecimal PLOT_AREA_3000 = BigDecimal.valueOf(3000);
	private static final BigDecimal PLOT_AREA_10000 = BigDecimal.valueOf(10000);
	private static final BigDecimal PLOT_AREA_20000 = BigDecimal.valueOf(20000);
	private static final BigDecimal PLOT_AREA_1080 = BigDecimal.valueOf(20000);
	private static final BigDecimal PLOT_AREA_510 = BigDecimal.valueOf(20000);
	private static final BigDecimal PLOT_AREA_2000 = BigDecimal.valueOf(2000);
	private static final BigDecimal PLOT_AREA_400 = BigDecimal.valueOf(400);
	private static final BigDecimal PLOT_AREA_4000 = BigDecimal.valueOf(4000);
	private static final BigDecimal PLOT_AREA_12000 = BigDecimal.valueOf(12000);
	private static final BigDecimal PLOT_AREA_450 = BigDecimal.valueOf(450);

	private static final BigDecimal COVERAGE_AREA_75 = BigDecimal.valueOf(0.75);
	private static final BigDecimal COVERAGE_AREA_70 = BigDecimal.valueOf(0.7);
	private static final BigDecimal COVERAGE_AREA_66 = BigDecimal.valueOf(0.66);
	private static final BigDecimal COVERAGE_AREA_65 = BigDecimal.valueOf(0.65);
	private static final BigDecimal COVERAGE_AREA_60 = BigDecimal.valueOf(0.6);
	private static final BigDecimal COVERAGE_AREA_55 = BigDecimal.valueOf(0.55);
	private static final BigDecimal COVERAGE_AREA_50 = BigDecimal.valueOf(0.5);
	private static final BigDecimal COVERAGE_AREA_45 = BigDecimal.valueOf(0.45);
	private static final BigDecimal COVERAGE_AREA_40 = BigDecimal.valueOf(0.4);
	private static final BigDecimal COVERAGE_AREA_35 = BigDecimal.valueOf(0.35);
	private static final BigDecimal COVERAGE_AREA_33 = BigDecimal.valueOf(0.33);
	private static final BigDecimal COVERAGE_AREA_33_33 = BigDecimal.valueOf(0.3333);
	private static final BigDecimal COVERAGE_AREA_30 = BigDecimal.valueOf(0.3);
	private static final BigDecimal COVERAGE_AREA_25 = BigDecimal.valueOf(0.25);
	private static final BigDecimal COVERAGE_AREA_20 = BigDecimal.valueOf(0.2);
	private static final BigDecimal COVERAGE_AREA_10 = BigDecimal.valueOf(0.1);

	@Override
	public Plan validate(Plan pl) {
		for (Block block : pl.getBlocks()) {
			if (block.getCoverage().isEmpty()) {
				pl.addError("coverageArea" + block.getNumber(),
						"Coverage Area for block " + block.getNumber() + " not Provided");
			}
		}
		return pl;
	}

	@Override
	public Plan process(Plan pl) {
try {

	validate(pl);
	String requiredPlotArea = "";
	// String requiredCoverageArea = "";
	BigDecimal totalCoverage = BigDecimal.ZERO;
	BigDecimal totalCoverageArea = BigDecimal.ZERO;
	BigDecimal coveragePercentage = BigDecimal.ZERO;
	BigDecimal PlotArea = pl.getPlot().getArea();
	// String OccupancyType=pl.getPlanInformation().getLandUseZone().toUpperCase();
	OccupancyTypeHelper mostRestrictiveOccupancyType = Util_AR.getMostRestrictive(pl);
	LOG.info("\n mostRestrictiveOccupancyType : " + mostRestrictiveOccupancyType.getType().getName());
	LOG.info("\n====================================\n");
	LOG.info(" PLOT area ; " + PlotArea);

	for (Block block : pl.getBlocks()) {
		BigDecimal coverageAreaWithoutDeduction = BigDecimal.ZERO;
		BigDecimal coverageDeductionArea = BigDecimal.ZERO;

		for (Measurement coverage : block.getCoverage()) {
			coverageAreaWithoutDeduction = coverageAreaWithoutDeduction.add(coverage.getArea());
		}
		for (Measurement deduct : block.getCoverageDeductions()) {
			coverageDeductionArea = coverageDeductionArea.add(deduct.getArea());
		}
		if (block.getBuilding() != null) {
			block.getBuilding().setCoverageArea(coverageAreaWithoutDeduction.subtract(coverageDeductionArea));
			BigDecimal coverage = BigDecimal.ZERO;
			if (pl.getPlot().getArea().doubleValue() > 0)
				coverage = block.getBuilding().getCoverageArea().multiply(BigDecimal.valueOf(100)).divide(
						pl.getPlanInformation().getPlotArea(), DcrConstants.DECIMALDIGITS_MEASUREMENTS,
						DcrConstants.ROUNDMODE_MEASUREMENTS);

			block.getBuilding().setCoverage(coverage);

			totalCoverageArea = totalCoverageArea.add(block.getBuilding().getCoverageArea());
			// totalCoverage =
			// totalCoverage.add(block.getBuilding().getCoverage());
		}

	}

	System.out.println("total coverage area : " + totalCoverageArea);

	if (pl.getPlot() != null && pl.getPlot().getArea().doubleValue() > 0)
		totalCoverage = totalCoverageArea.multiply(BigDecimal.valueOf(100)).divide(
				pl.getPlanInformation().getPlotArea(), DcrConstants.DECIMALDIGITS_MEASUREMENTS,
				DcrConstants.ROUNDMODE_MEASUREMENTS);
	pl.setCoverage(totalCoverage);

	System.out.println("totalCoverage : " + totalCoverage);

	if (pl.getVirtualBuilding() != null) {
		pl.getVirtualBuilding().setTotalCoverageArea(totalCoverageArea);
		pl.getVirtualBuilding().setMostRestrictiveFarHelper(mostRestrictiveOccupancyType);
	}

	BigDecimal roadWidth = pl.getPlanInformation().getRoadWidth();
	if (roadWidth != null && roadWidth.compareTo(ROAD_WIDTH_TWELVE_POINTTWO) >= 0
			&& roadWidth.compareTo(ROAD_WIDTH_THIRTY_POINTFIVE) <= 0) {
		processCoverage(pl, StringUtils.EMPTY, totalCoverage, Forty, null);
	}

	if (mostRestrictiveOccupancyType.getType().getCode().equals(A)) {

		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(A_R)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(A_AF)) {

			if (PlotArea.compareTo(PLOT_AREA_48) <= 0) {

			} else if (PlotArea.compareTo(PLOT_AREA_48) > 0 && PlotArea.compareTo(PLOT_AREA_60) <= 0) {

				requiredPlotArea = "Above 48 to 60";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_75);

			} else if (PlotArea.compareTo(PLOT_AREA_60) > 0 && PlotArea.compareTo(PLOT_AREA_100) <= 0) {
				requiredPlotArea = "Above 60 to 100";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_65);

			} else if (PlotArea.compareTo(PLOT_AREA_100) > 0 && PlotArea.compareTo(PLOT_AREA_250) <= 0) {
				requiredPlotArea = "Above 100 to 250";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_65);
			} else if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
				requiredPlotArea = "Above 250 to 500";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_60);
			} else if (PlotArea.compareTo(PLOT_AREA_500) > 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
				requiredPlotArea = "Above 500 to 1000";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_50);
			} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
				requiredPlotArea = " Above 1000 to 1500";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_50);
			} else if (PlotArea.compareTo(PLOT_AREA_1500) > 0 && PlotArea.compareTo(PLOT_AREA_3000) <= 0) {
				requiredPlotArea = "Above 1500 to 3000";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_45);
			}

		}

		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(A_RH)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_75);
		}

		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(A_HE)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(A_BH)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(A_LH)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(A_GH)) {

			if (PlotArea.compareTo(PLOT_AREA_100) > 0 && PlotArea.compareTo(PLOT_AREA_250) <= 0) {
				requiredPlotArea = "Above 100 to 250";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_65);
			} else if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
				requiredPlotArea = "Above 250 to 500";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_60);

			} else if (PlotArea.compareTo(PLOT_AREA_500) > 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
				requiredPlotArea = "Above 500 to 1000";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_50);
			} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
				requiredPlotArea = "Above 1000 to 1500";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_50);
			} else if (PlotArea.compareTo(PLOT_AREA_1500) > 0 && PlotArea.compareTo(PLOT_AREA_3000) <= 0) {
				requiredPlotArea = "Above 1500 to 3000";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_45);
			}

		}

	} else if (mostRestrictiveOccupancyType.getType().getCode().equals(F)) {

		// Restaurants/shops
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(F_RT)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(F_SH)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(F_CB)) {

			if (PlotArea.compareTo(PLOT_AREA_48) > 0 && PlotArea.compareTo(PLOT_AREA_100) <= 0) {
				requiredPlotArea = "Above 48 to 100";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_75);
			} else if (PlotArea.compareTo(PLOT_AREA_100) > 0 && PlotArea.compareTo(PLOT_AREA_250) <= 0) {
				requiredPlotArea = "Above 100 to 250";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_65);
			} else if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
				requiredPlotArea = "Above 250 to 500";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_60);

			} else if (PlotArea.compareTo(PLOT_AREA_500) > 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
				requiredPlotArea = "Above 500 to 1000";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_50);
			} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
				requiredPlotArea = "Above 1000 to 1500";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_50);
			} else if (PlotArea.compareTo(PLOT_AREA_1500) > 0 && PlotArea.compareTo(PLOT_AREA_3000) <= 0) {
				requiredPlotArea = "Above 1500 to 3000";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_45);
			}

		}
		// shopping mall
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(F_SM)) {
			if (PlotArea.compareTo(PLOT_AREA_450) > 0) {
				requiredPlotArea = "Above 450";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_40);
			}

		}
		// Hotel
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(F_H)) {

			if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
				requiredPlotArea = "Above 250 to 500";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_60);
			} else if (PlotArea.compareTo(PLOT_AREA_500) > 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
				requiredPlotArea = "Above 500 to 1000";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_50);
			} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
				requiredPlotArea = "Above 1000 to 1500";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_50);
			} else if (PlotArea.compareTo(PLOT_AREA_1500) > 0) {
				requiredPlotArea = "Above 1500";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_40);
			}

		}
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(F_M)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_35);

		}
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(F_R)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_35);
		}

		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(F_PTP)) {
			if (mostRestrictiveOccupancyType.getUsage().getCode().equals(F_PTP_FCSS)) {
				if (PlotArea.compareTo(PLOT_AREA_1080) > 0) {
					requiredPlotArea = "Above 1080";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_20);
				}
			}
			if (mostRestrictiveOccupancyType.getUsage().getCode().equals(F_PTP_FS)) {
				if (PlotArea.compareTo(PLOT_AREA_510) > 0) {
					requiredPlotArea = "Above 510";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_10);
				}
			}

		}
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(F_WH)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(F_WST)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_20);
		}

	} else if (mostRestrictiveOccupancyType.getType().getCode().equals(ML)) {

	} else if (mostRestrictiveOccupancyType.getType().getCode().equals(G)) {

		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(G_SC)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(G_FI)) {
			if (PlotArea.compareTo(PLOT_AREA_2000) >= 0) {
				requiredPlotArea = "Above 2000";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_33_33);
			}

		}

		// check for light and service industry
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(G_LSI)) {
			if (PlotArea.compareTo(PLOT_AREA_400) < 0) {
				requiredPlotArea = "Below 400 ";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_60);
			} else if (PlotArea.compareTo(PLOT_AREA_400) > 0 && PlotArea.compareTo(PLOT_AREA_4000) <= 0) {
				requiredPlotArea = "Above 400 to 4000";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_50);
			} else if (PlotArea.compareTo(PLOT_AREA_4000) > 0 && PlotArea.compareTo(PLOT_AREA_12000) <= 0) {
				requiredPlotArea = "Above 4000 to 12000";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_45);
			} else if (PlotArea.compareTo(PLOT_AREA_12000) > 0) {
				requiredPlotArea = "Above 12000";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_40);
			}

		}
	} else if (mostRestrictiveOccupancyType.getType().getCode().equals(T)) {

		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(T_R)) {
			if (pl.getPlanInfoProperties().get("TRANSPORTATION_AREA_TYPE").equalsIgnoreCase("OPERATION")) {

				requiredPlotArea = "-";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_70);

			} else if (pl.getPlanInfoProperties().get("TRANSPORTATION_AREA_TYPE").equalsIgnoreCase("BUILDING")) {

				requiredPlotArea = "-";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_30);

			}
		}
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(T_I)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(T_B)) {

			if (pl.getPlanInfoProperties().get("TRANSPORTATION_AREA_TYPE").equalsIgnoreCase("OPERATION")) {
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(T_I)) {

					requiredPlotArea = "-";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_25);

				} else if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(T_B)) {
					requiredPlotArea = "-";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_50);

				}

			} else if (pl.getPlanInfoProperties().get("TRANSPORTATION_AREA_TYPE").equalsIgnoreCase("BUILDING")) {

				requiredPlotArea = "-";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_50);

			}

		}

	} else if (mostRestrictiveOccupancyType.getType().getCode().equals(P)) {

		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P_O)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(P_I)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_40);
		}

		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P_D)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_40);
		}
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P_H)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(P_A)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(P_B)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_30);
		}

		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P_C)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_35);
		}

	} else if (mostRestrictiveOccupancyType.getType().getCode().equals(C)) {

		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C_NH)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(C_PC)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(C_D)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(C_DC)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_30);

		}
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C_VH)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(C_VD)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_30);
		}

		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C_NAPI)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_35);

		}
	} else if (mostRestrictiveOccupancyType.getType().getCode().equals(B)) {

		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(B_NS)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_40);
		}
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(B_PS)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(B_UPS)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_40);
		}
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(B_HSS)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(B_SS)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_40);
		}
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(B_C)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(B_U)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_35);
		}

		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(B_SFMC)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_50);
		}
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(B_ERIC)) {

			if (mostRestrictiveOccupancyType.getUsage().getCode().equals(B_ERIC_AC)) {
				if (PlotArea.compareTo(BigDecimal.valueOf(0.45).multiply(PlotArea)) >= 0) {
					requiredPlotArea = "Above 45% of Total Area";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_30);
				}
			} else if (mostRestrictiveOccupancyType.getUsage().getCode().equals(B_ERIC_AR)) {

			} else if (mostRestrictiveOccupancyType.getUsage().getCode().equals(B_ERIC_SCC)) {
				if (PlotArea.compareTo(BigDecimal.valueOf(0.15).multiply(PlotArea)) >= 0) {
					requiredPlotArea = "Above 15% of Total Area";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_10);
				}
			} else if (mostRestrictiveOccupancyType.getUsage().getCode().equals(B_ERIC_POS)) {
				if (PlotArea.compareTo(BigDecimal.valueOf(0.15).multiply(PlotArea)) >= 0) {
					requiredPlotArea = "Above 15% of Total Area";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_10);
				}
			}

		}
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(B_SP)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_25);
		}

	} else if (mostRestrictiveOccupancyType.getType().getCode().equals(U)) {

		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(U_PP)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_40);
		}
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(U_PS)) {
			if (mostRestrictiveOccupancyType.getUsage().getCode().equals(U_PS_DOB)) {
				requiredPlotArea = "-";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_40);
			}
			if (mostRestrictiveOccupancyType.getUsage().getCode().equals(U_PS_DJ)) {
				requiredPlotArea = "-";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_30);
			}
			if (mostRestrictiveOccupancyType.getUsage().getCode().equals(U_PS_PTI)) {
				requiredPlotArea = "-";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_30);
			}

		}
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(U_DMC)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_40);

		}
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(U_FP)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(U_FS)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(U_FTI)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(U_FTC)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_40);
		}
	}

//	if (requiredPlotArea != "" && requiredPlotArea != null) {
		processCoverage(pl, StringUtils.EMPTY, totalCoverageArea, coveragePercentage, requiredPlotArea);
//	}
} catch (Exception e) {
	// TODO: handle exception
}
		/*
		 * BigDecimal ZONE_DC = BigDecimal.ZERO; String STABILITY_REPORT =
		 * StringUtils.EMPTY;
		 * 
		 * try { ZONE_DC = new BigDecimal(pl.getPlanInfoProperties().entrySet().stream()
		 * .filter(e ->
		 * e.getKey().equals("STABILITY_ZONE")).map(Map.Entry::getValue).findFirst().
		 * orElse("0")); STABILITY_REPORT = (String)
		 * pl.getPlanInfoProperties().entrySet().stream() .filter(e ->
		 * e.getKey().equals("STABILITY_REPORT")).map(Map.Entry::getValue).findFirst().
		 * orElse("0"); }catch(Exception e){ ZONE_DC = BigDecimal.ZERO; }
		 */


		return pl;
	}

	private void processCoverage(Plan pl, String occupancy, BigDecimal coverage, BigDecimal upperLimit,
			String expectedResult) {
		ScrutinyDetail scrutinyDetail = new ScrutinyDetail();
		scrutinyDetail.setKey("Common_Coverage");
		scrutinyDetail.setHeading("Coverage in Percentage");
		scrutinyDetail.addColumnHeading(1, RULE_NO);
		scrutinyDetail.addColumnHeading(2, DESCRIPTION);
		scrutinyDetail.addColumnHeading(4, PERMISSIBLE);
		scrutinyDetail.addColumnHeading(5, PROVIDED);
		scrutinyDetail.addColumnHeading(6, STATUS);

		String desc = "Coverage";
		String actualResult = getLocaleMessage(RULE_ACTUAL_KEY, coverage.toString());
		if (expectedResult.equalsIgnoreCase("")) {
			expectedResult = "Not Validated";
		} else {
			expectedResult = "Should be <= " + upperLimit;
		}

		Map<String, String> details = new HashMap<>();
		details.put(RULE_NO, RULE_38);
		details.put(DESCRIPTION, desc);
		details.put(PERMISSIBLE, expectedResult);
		details.put(PROVIDED, actualResult);

		if (coverage.doubleValue() <= upperLimit.doubleValue()) {
			details.put(STATUS, Result.Accepted.getResultVal());
		} else {
			details.put(STATUS, Result.Not_Accepted.getResultVal());
		}

		scrutinyDetail.getDetail().add(details);
		pl.getReportOutput().getScrutinyDetails().add(scrutinyDetail);
	}

}
