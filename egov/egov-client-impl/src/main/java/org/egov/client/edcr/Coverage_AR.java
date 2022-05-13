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
	String requiredPlotArea ="";
	BigDecimal totalCoverage = BigDecimal.ZERO;
	BigDecimal totalCoverageArea = BigDecimal.ZERO;
	BigDecimal coveragePercentage = BigDecimal.ZERO;
	BigDecimal PlotArea = pl.getPlot().getArea();
	OccupancyTypeHelper mostRestrictiveOccupancyType = Util_AR.getMostRestrictive(pl);

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
		}

	}


	if (pl.getPlot() != null && pl.getPlot().getArea().doubleValue() > 0)
		totalCoverage = totalCoverageArea.multiply(BigDecimal.valueOf(100)).divide(
				pl.getPlanInformation().getPlotArea(), DcrConstants.DECIMALDIGITS_MEASUREMENTS,
				DcrConstants.ROUNDMODE_MEASUREMENTS);
	pl.setCoverage(totalCoverage);


	if (pl.getVirtualBuilding() != null) {
		pl.getVirtualBuilding().setTotalCoverageArea(totalCoverageArea);
		pl.getVirtualBuilding().setMostRestrictiveFarHelper(mostRestrictiveOccupancyType);
	}

	
	if (mostRestrictiveOccupancyType != null) {
		if ((mostRestrictiveOccupancyType.getType() != null
				&& R.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode()))) {
			processCoverageResidential(pl, mostRestrictiveOccupancyType,  PlotArea,totalCoverage,requiredPlotArea,coveragePercentage );
		}
		if (mostRestrictiveOccupancyType.getType() != null
				&& C.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {
			processCoverageCommercial(pl, mostRestrictiveOccupancyType,  PlotArea, totalCoverage,requiredPlotArea,coveragePercentage);
		}
		if (mostRestrictiveOccupancyType.getType() != null
				&& I.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

			processCoverageIndustrial(pl, mostRestrictiveOccupancyType,  PlotArea, totalCoverage,requiredPlotArea,coveragePercentage);
		}
		if (mostRestrictiveOccupancyType.getType() != null
				&& G.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

			processCoverageGovernmentUse(pl, mostRestrictiveOccupancyType,  PlotArea, totalCoverage,requiredPlotArea,coveragePercentage);
		}
		if (mostRestrictiveOccupancyType.getType() != null
				&& T.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

			processCoverageTransportation(pl, mostRestrictiveOccupancyType,  PlotArea, totalCoverage,requiredPlotArea,coveragePercentage);
		}
		if (mostRestrictiveOccupancyType.getType() != null
				&& P.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

			processCoveragePublicSemiPublic(pl, mostRestrictiveOccupancyType,  PlotArea, totalCoverage,requiredPlotArea,coveragePercentage);
		}

	}
	
	

} catch (Exception e) {
	// TODO: handle exception
}


		return pl;
	}

	

	private void processCoveragePublicSemiPublic(Plan pl, OccupancyTypeHelper mostRestrictiveOccupancyType,
			BigDecimal PlotArea, BigDecimal totalCoverage, String requiredPlotArea, BigDecimal coveragePercentage) {
		// Meeting Hall, Auditorium, Community Center
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P3a)) {
					requiredPlotArea = "-";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_30);

				}
				
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P3b)) {
					requiredPlotArea = "-";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_35);

				}
				//Hostel
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P3c)) {
					if(PlotArea.compareTo(PLOT_AREA_100)<0) {
						pl.addError("PlotArea below 100 Hostel","Plot area below 100 sqmts not allowed for hostel");
					}
					else if (PlotArea.compareTo(PLOT_AREA_100) > 0 && PlotArea.compareTo(PLOT_AREA_250) <= 0) {
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
					}else if(PlotArea.compareTo(PLOT_AREA_3000)>0) {
						pl.addError("PlotArea above 3000 Hostel","Plot area above 3000 sqmts not allowed for hostel");
					}

				}
				


				// Nursing Center
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P2b)) {
					requiredPlotArea = "-";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_30);
				}

				// Veterinary
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P2c)) {
					requiredPlotArea = "-";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_30);
				}

				// Medical College, Nursing and paramedic Institute
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P2d)) {
					requiredPlotArea = "-";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_35);
				}

				// Nursery School
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P1b)) {
					requiredPlotArea = "-";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_40);

				}

				// Primary School
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P1c)) {
					requiredPlotArea = "-";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_40);
				}

				// High / Higher Secondary
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P1d)) {
					requiredPlotArea = "-";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_40);
				}

				// College
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P1e)) {
					requiredPlotArea = "-";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_35);
				}

				// Special School
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P1g)) {
					requiredPlotArea = "-";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_50);
				}

				// Edu & Research Centre
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P1f)) {
					requiredPlotArea = "-";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_30);
				}

				// Sports
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P1a)) {
					requiredPlotArea = "-";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_25);
				}
				if(requiredPlotArea!="")
					processCoverage(pl, mostRestrictiveOccupancyType.getSubtype().getName(), totalCoverage, coveragePercentage, requiredPlotArea);
	}

	private void processCoverageTransportation(Plan pl, OccupancyTypeHelper mostRestrictiveOccupancyType,
			BigDecimal plotArea, BigDecimal totalCoverage, String requiredPlotArea, BigDecimal coveragePercentage) {
		// Rail Terminal
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(T1b)) {
					if (pl.getPlanInfoProperties().get("TRANSPORTATION_AREA_TYPE").equalsIgnoreCase("OPERATION")) {

						requiredPlotArea = "-";
						coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_70);

					} else if (pl.getPlanInfoProperties().get("TRANSPORTATION_AREA_TYPE").equalsIgnoreCase("BUILDING")) {

						requiredPlotArea = "-";
						coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_30);

					}

				}
				// ISBT Bus Terminal
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(T1a)
						|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(T1c)) {
					if (pl.getPlanInfoProperties().get("TRANSPORTATION_AREA_TYPE").equalsIgnoreCase("OPERATION")) {
						if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(T1a)) {

							requiredPlotArea = "-";
							coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_25);

						} else if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(T1c)) {
							requiredPlotArea = "-";
							coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_50);

						}

					} else if (pl.getPlanInfoProperties().get("TRANSPORTATION_AREA_TYPE").equalsIgnoreCase("BUILDING")) {

						requiredPlotArea = "-";
						coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_50);

					}

				}
				if(requiredPlotArea!="")
					processCoverage(pl, mostRestrictiveOccupancyType.getSubtype().getName(), totalCoverage, coveragePercentage, requiredPlotArea);
	}

	private void processCoverageGovernmentUse(Plan pl, OccupancyTypeHelper mostRestrictiveOccupancyType, BigDecimal plotArea,
			BigDecimal totalCoverage, String requiredPlotArea, BigDecimal coveragePercentage) {
		// General/Govt./ Integrated Office Complex
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(G2a)) {
					requiredPlotArea = "-";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_40);

				}
				// District Court
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(G1a)) {
					requiredPlotArea = "-";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_40);

				}
				// Police Post
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(G3a)) {
					requiredPlotArea = "-";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_30);
				}
				// Police Station
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(G3b)) {
					requiredPlotArea = "-";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_40);
				}
				// Other safety departments,Disaster manangement center, Fire Station etc.
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(G3c)) {
					requiredPlotArea = "-";
					coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_40);

				}
				if(requiredPlotArea!="")
					processCoverage(pl, mostRestrictiveOccupancyType.getSubtype().getName(), totalCoverage, coveragePercentage, requiredPlotArea);
	}

	private void processCoverageIndustrial(Plan pl, OccupancyTypeHelper mostRestrictiveOccupancyType, BigDecimal PlotArea,
			BigDecimal totalCoverage, String requiredPlotArea, BigDecimal coveragePercentage) {
		// Flatted
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(I1a)) {
					if(PlotArea.compareTo(PLOT_AREA_2000)<0) {
						pl.addError("PlotArea below 2000 Flatted","Plot area below 2000 sqmts not allowed for Flatted");
					}
					else if (PlotArea.compareTo(PLOT_AREA_2000) >= 0) {
						requiredPlotArea = "Above 2000";
						coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_33_33);
					}

				}
				
		// Light and Service
				if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(I1b)) {
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
				if(requiredPlotArea!="")
					processCoverage(pl, mostRestrictiveOccupancyType.getSubtype().getName(), totalCoverage, coveragePercentage, requiredPlotArea);
	}

	private void processCoverageCommercial(Plan pl, OccupancyTypeHelper mostRestrictiveOccupancyType, BigDecimal PlotArea,
			BigDecimal totalCoverage, String requiredPlotArea, BigDecimal coveragePercentage) {
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C1a)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(C1b)) {
			if(PlotArea.compareTo(PLOT_AREA_48)<0) {
				pl.addError("PlotArea below 48 commmercial","Plot area below 48 sqmts not allowed for Shops/Restaurant");
			}
			else if (PlotArea.compareTo(PLOT_AREA_48) > 0 && PlotArea.compareTo(PLOT_AREA_100) <= 0) {
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
			}else if(PlotArea.compareTo(PLOT_AREA_3000)>0) {
				pl.addError("PlotArea Above 3000 Commercial","Plot area should not exceed above 3000 sqmts for Restaurant/Shops");
			}

		}
		
		//Shopping Mall
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C1c)) {
			if(PlotArea.compareTo(PLOT_AREA_450)<0) {
				pl.addError("PlotArea below 450 Shopping","Plot area below 450 sqmts not allowed for Shopping");
			}
			else if (PlotArea.compareTo(PLOT_AREA_450) >= 0 ) {
				requiredPlotArea = "Above 450";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_40);
			}
		}
		
		// Hotel
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C2a)) {
			if(PlotArea.compareTo(PLOT_AREA_250)<0){
				pl.addError("PlotArea below 250 Hotel","Plot area below 250 sqmts not allowed for hotel");
			}
			else if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
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
		// Motel
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C3a)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_35);

		}
		// Resort
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C4a)) {
				requiredPlotArea = "-";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_35);
		}
		// Petrol Pump
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C5a)) {
			if (PlotArea.compareTo(PLOT_AREA_1080) > 0) {
				requiredPlotArea = "Above 1080";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_20);
			} else if (PlotArea.compareTo(PLOT_AREA_510) > 0) {
				requiredPlotArea = "Above 510";
				coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_10);

			}else {
				pl.addError("PlotArea below 510 Petrol","Plot area below 510 sqmts not allowed for Petrol Pump");
			}

		}
		// Wholesale
		if (mostRestrictiveOccupancyType.getUsage().getCode().equals(C6a)) {
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_20);

		}
		if(requiredPlotArea!="")
			processCoverage(pl, mostRestrictiveOccupancyType.getSubtype().getName(), totalCoverage, coveragePercentage, requiredPlotArea);
	}

	private void processCoverageResidential(Plan pl, OccupancyTypeHelper mostRestrictiveOccupancyType, BigDecimal PlotArea,
			BigDecimal totalCoverage, String requiredPlotArea, BigDecimal coveragePercentage) {
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(R1a)){
			requiredPlotArea = "-";
			coveragePercentage = (pl.getPlot().getArea()).multiply(COVERAGE_AREA_75);
		}
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(R1b)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(R1c)) {
			if(PlotArea.compareTo(PLOT_AREA_48)<0) {
				pl.addError("PlotArea below 48 Residential","Plot area below 48 sqmts not allowed for plotted residential housing");
			}
			else if (PlotArea.compareTo(PLOT_AREA_48) > 0 && PlotArea.compareTo(PLOT_AREA_60) <= 0) {
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
			}else if (PlotArea.compareTo(PLOT_AREA_3000) > 0) {
				pl.addError("PlotArea Above 3000 Residential","Plot area should not exceed above 3000 sqmts for plotted residential housing");
			}
		}
		if(requiredPlotArea!="")
			processCoverage(pl, mostRestrictiveOccupancyType.getSubtype().getName(), totalCoverage, coveragePercentage, requiredPlotArea);
		
		
	}

	private void processCoverage(Plan pl, String occupancy, BigDecimal coverage, BigDecimal expectedResult,
			String requiredPlotArea) {
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
		

		Map<String, String> details = new HashMap<>();
		details.put(RULE_NO, RULE_38);
		details.put(DESCRIPTION, desc);
		details.put(PERMISSIBLE,"<= "+ expectedResult.toString());
		details.put(PROVIDED, actualResult);

		if (coverage.doubleValue() <= expectedResult.doubleValue()) {
			details.put(STATUS, Result.Accepted.getResultVal());
		} else {
			details.put(STATUS, Result.Not_Accepted.getResultVal());
		}

		scrutinyDetail.getDetail().add(details);
		pl.getReportOutput().getScrutinyDetails().add(scrutinyDetail);
	}

}
