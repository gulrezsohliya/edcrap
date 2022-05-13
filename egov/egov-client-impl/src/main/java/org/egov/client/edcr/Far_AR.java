/*
 * eGov  SmartCity eGovernance suite aims to improve the internal efficiency,transparency,
 * accountability and the service delivery of the government  organizations.
 *
 *  Copyright (C) <2019>  eGovernments Foundation
 *
 *  The updated version of eGov suite of products as by eGovernments Foundation
 *  is available at http://www.egovernments.org
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see http://www.gnu.org/licenses/ or
 *  http://www.gnu.org/licenses/gpl.html .
 *
 *  In addition to the terms of the GPL license to be adhered to in using this
 *  program, the following additional terms are to be complied with:
 *
 *      1) All versions of this program, verbatim or modified must carry this
 *         Legal Notice.
 *      Further, all user interfaces, including but not limited to citizen facing interfaces,
 *         Urban Local Bodies interfaces, dashboards, mobile applications, of the program and any
 *         derived works should carry eGovernments Foundation logo on the top right corner.
 *
 *      For the logo, please refer http://egovernments.org/html/logo/egov_logo.png.
 *      For any further queries on attribution, including queries on brand guidelines,
 *         please contact contact@egovernments.org
 *
 *      2) Any misrepresentation of the origin of the material is prohibited. It
 *         is required that all modified versions of this material be marked in
 *         reasonable ways as different from the original version.
 *
 *      3) This license does not grant any rights to any user of the program
 *         with regards to rights under trademark law for use of the trade names
 *         or trademarks of eGovernments Foundation.
 *
 *  In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 */

package org.egov.client.edcr;

import static org.egov.edcr.utility.DcrConstants.DECIMALDIGITS_MEASUREMENTS;
import static org.egov.edcr.utility.DcrConstants.OBJECTNOTDEFINED;
import static org.egov.edcr.utility.DcrConstants.PLOT_AREA;
import static org.egov.edcr.utility.DcrConstants.ROUNDMODE_MEASUREMENTS;

//from  DxfFileConstants_AR
import static org.egov.client.constants.DxfFileConstants_AR.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
//import org.egov.client.constants.DxfFileConstants_AR;
import org.egov.common.entity.edcr.Block;
import org.egov.common.entity.edcr.Building;
import org.egov.common.entity.edcr.FarDetails;
import org.egov.common.entity.edcr.Floor;
import org.egov.common.entity.edcr.Measurement;
import org.egov.common.entity.edcr.Occupancy;
import org.egov.common.entity.edcr.OccupancyTypeHelper;
import org.egov.common.entity.edcr.Plan;
import org.egov.common.entity.edcr.Plot;
import org.egov.common.entity.edcr.Result;
import org.egov.common.entity.edcr.ScrutinyDetail;
import org.egov.edcr.constants.DxfFileConstants;
import org.egov.edcr.feature.Far;
import org.egov.edcr.service.ProcessPrintHelper;
import org.egov.edcr.utility.DcrConstants;
import org.egov.infra.utils.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class Far_AR extends Far {

	private static final Logger LOG = Logger.getLogger(Far_AR.class);

	private static final String VALIDATION_NEGATIVE_FLOOR_AREA = "msg.error.negative.floorarea.occupancy.floor";
	private static final String VALIDATION_NEGATIVE_EXISTING_FLOOR_AREA = "msg.error.negative.existing.floorarea.occupancy.floor";
	private static final String VALIDATION_NEGATIVE_BUILTUP_AREA = "msg.error.negative.builtuparea.occupancy.floor";
	private static final String VALIDATION_NEGATIVE_EXISTING_BUILTUP_AREA = "msg.error.negative.existing.builtuparea.occupancy.floor";
	public static final String RULE_31_1 = "31-1";
	public static final String RULE_38 = "38";
	// old
	private static final BigDecimal POINTTWO = BigDecimal.valueOf(0.2);
	private static final BigDecimal POINTFOUR = BigDecimal.valueOf(0.4);
	private static final BigDecimal POINTFIVE = BigDecimal.valueOf(0.5);
	private static final BigDecimal POINTSIX = BigDecimal.valueOf(0.6);
	private static final BigDecimal POINTSEVEN = BigDecimal.valueOf(0.7);
	private static final BigDecimal POINTEIGHT = BigDecimal.valueOf(0.7);
	private static final BigDecimal ONE = BigDecimal.valueOf(1);
	private static final BigDecimal ONE_POINTTWO = BigDecimal.valueOf(1.2);
	private static final BigDecimal ONE_POINTFIVE = BigDecimal.valueOf(1.5);
	private static final BigDecimal ONE_POINTEIGHT = BigDecimal.valueOf(1.8);
	private static final BigDecimal ONE_POINTTWOFIVE = BigDecimal.valueOf(1.25);
	private static final BigDecimal TWO = BigDecimal.valueOf(2);
	private static final BigDecimal TWO_POINTFIVE = BigDecimal.valueOf(2.5);
	private static final BigDecimal THREE = BigDecimal.valueOf(3);
	private static final BigDecimal THREE_POINTTWOFIVE = BigDecimal.valueOf(3.25);
	private static final BigDecimal THREE_POINTFIVE = BigDecimal.valueOf(3.5);
	private static final BigDecimal FIFTEEN = BigDecimal.valueOf(15);
	// new for AP
//	private static final BigDecimal POINT_TWO = BigDecimal.valueOf(10).divide(BigDecimal.valueOf(100));
	private static final BigDecimal POINTONE = BigDecimal.valueOf(10).divide(BigDecimal.valueOf(100));
	private static final BigDecimal POINTONEFIVE = BigDecimal.valueOf(10).divide(BigDecimal.valueOf(100));
//	private static final BigDecimal TWENTY = BigDecimal.valueOf(20).divide(BigDecimal.valueOf(100));
//	private static final BigDecimal FORTY = BigDecimal.valueOf(40).divide(BigDecimal.valueOf(100));
	private static final BigDecimal POINT_FOUR = BigDecimal.valueOf(40).divide(BigDecimal.valueOf(100));
	private static final BigDecimal FORTY_FIVE = BigDecimal.valueOf(40).divide(BigDecimal.valueOf(100));
	private static final BigDecimal FOUR_POINTFIVE = BigDecimal.valueOf(40).divide(BigDecimal.valueOf(100));
	private static final BigDecimal FIFTY = BigDecimal.valueOf(50).divide(BigDecimal.valueOf(100));
	private static final BigDecimal FIVE = BigDecimal.valueOf(50).divide(BigDecimal.valueOf(100));
	private static final BigDecimal SIXTY = BigDecimal.valueOf(60).divide(BigDecimal.valueOf(100));
	private static final BigDecimal SIX = BigDecimal.valueOf(60).divide(BigDecimal.valueOf(100));
	private static final BigDecimal SEVENTY = BigDecimal.valueOf(70).divide(BigDecimal.valueOf(100));
	private static final BigDecimal SEVEN = BigDecimal.valueOf(70).divide(BigDecimal.valueOf(100));
	private static final BigDecimal EIGHTY = BigDecimal.valueOf(80).divide(BigDecimal.valueOf(100));
	private static final BigDecimal EIGHT = BigDecimal.valueOf(80).divide(BigDecimal.valueOf(100));
//	private static final BigDecimal TWO = BigDecimal.valueOf(100).divide(BigDecimal.valueOf(100));
//	private static final BigDecimal POINT_FOUR = BigDecimal.valueOf(120).divide(BigDecimal.valueOf(100));
//	private static final BigDecimal ONE_FIFTY = BigDecimal.valueOf(150).divide(BigDecimal.valueOf(100));
	private static final BigDecimal ONE_FIFTYFIVE = BigDecimal.valueOf(150).divide(BigDecimal.valueOf(100));
	private static final BigDecimal ONE_POINTFIVEFIVE = BigDecimal.valueOf(150).divide(BigDecimal.valueOf(100));

	private static final BigDecimal ONE_SIXTY = BigDecimal.valueOf(160).divide(BigDecimal.valueOf(100));
	private static final BigDecimal ONE_POINTSIX = BigDecimal.valueOf(160).divide(BigDecimal.valueOf(100));
	private static final BigDecimal ONE_SEVENTYFIVE = BigDecimal.valueOf(175).divide(BigDecimal.valueOf(100));
	private static final BigDecimal ONE_POINTSEVENFIVE = BigDecimal.valueOf(175).divide(BigDecimal.valueOf(100));
	private static final BigDecimal ONE_EIGHTY = BigDecimal.valueOf(180).divide(BigDecimal.valueOf(100));
//	private static final BigDecimal ONE_POINTEIGHT = BigDecimal.valueOf(180).divide(BigDecimal.valueOf(100));
//	private static final BigDecimal TWO_HUNDRED = BigDecimal.valueOf(200).divide(BigDecimal.valueOf(100));
	private static final BigDecimal TWO_TWENTYFIVE = BigDecimal.valueOf(225).divide(BigDecimal.valueOf(100));
	private static final BigDecimal TWO_POINTTWOFIVE = BigDecimal.valueOf(225).divide(BigDecimal.valueOf(100));
	private static final BigDecimal POINTSEVENFIVE = BigDecimal.valueOf(75).divide(BigDecimal.valueOf(100));
//	private static final BigDecimal THREE_HUNDRED = BigDecimal.valueOf(300).divide(BigDecimal.valueOf(100));

	// AREA for AP
	private static final BigDecimal PLOT_AREA_48 = BigDecimal.valueOf(48);
	private static final BigDecimal PLOT_AREA_60 = BigDecimal.valueOf(60);
	private static final BigDecimal PLOT_AREA_100 = BigDecimal.valueOf(100);
	private static final BigDecimal PLOT_AREA_250 = BigDecimal.valueOf(250);
	private static final BigDecimal PLOT_AREA_500 = BigDecimal.valueOf(500);
	private static final BigDecimal PLOT_AREA_1000 = BigDecimal.valueOf(1000);
	private static final BigDecimal PLOT_AREA_1500 = BigDecimal.valueOf(1500);
	private static final BigDecimal PLOT_AREA_3000 = BigDecimal.valueOf(3000);
	private static final BigDecimal PLOT_AREA_5000 = BigDecimal.valueOf(5000);
	private static final BigDecimal PLOT_AREA_2000 = BigDecimal.valueOf(2000);
	private static final BigDecimal PLOT_AREA_4000 = BigDecimal.valueOf(4000);
	private static final BigDecimal PLOT_AREA_12000 = BigDecimal.valueOf(12000);
	private static final BigDecimal PLOT_AREA_28000 = BigDecimal.valueOf(28000);
	private static final BigDecimal PLOT_AREA_6000 = BigDecimal.valueOf(6000);
	private static final BigDecimal PLOT_AREA_20000 = BigDecimal.valueOf(20000);
	private static final BigDecimal PLOT_AREA_10000 = BigDecimal.valueOf(10000);
	private static final BigDecimal PLOT_AREA_1080 = BigDecimal.valueOf(1080);
	private static final BigDecimal PLOT_AREA_510 = BigDecimal.valueOf(510);
	private static final BigDecimal PLOT_AREA_400 = BigDecimal.valueOf(400);

	// Height for AP
	public static final BigDecimal HEIGHT_6 = BigDecimal.valueOf(6);
	public static final BigDecimal HEIGHT_9 = BigDecimal.valueOf(9);
	public static final BigDecimal HEIGHT_12 = BigDecimal.valueOf(12);
	public static final BigDecimal HEIGHT_15 = BigDecimal.valueOf(15);
	public static final BigDecimal HEIGHT_18 = BigDecimal.valueOf(18);

	// old road
	private static final BigDecimal ROAD_WIDTH_TWO_POINTFOUR = BigDecimal.valueOf(2.4);
	private static final BigDecimal ROAD_WIDTH_TWO_POINTFOURFOUR = BigDecimal.valueOf(2.44);
	private static final BigDecimal ROAD_WIDTH_THREE_POINTSIX = BigDecimal.valueOf(3.6);
	private static final BigDecimal ROAD_WIDTH_FOUR_POINTEIGHT = BigDecimal.valueOf(4.8);
	private static final BigDecimal ROAD_WIDTH_SIX_POINTONE = BigDecimal.valueOf(6.1);
	private static final BigDecimal ROAD_WIDTH_NINE_POINTONE = BigDecimal.valueOf(9.1);
	private static final BigDecimal ROAD_WIDTH_TWELVE_POINTTWO = BigDecimal.valueOf(12.2);

	private static final BigDecimal ROAD_WIDTH_EIGHTEEN_POINTTHREE = BigDecimal.valueOf(18.3);
	private static final BigDecimal ROAD_WIDTH_TWENTYFOUR_POINTFOUR = BigDecimal.valueOf(24.4);
	private static final BigDecimal ROAD_WIDTH_TWENTYSEVEN_POINTFOUR = BigDecimal.valueOf(27.4);
	private static final BigDecimal ROAD_WIDTH_THIRTY_POINTFIVE = BigDecimal.valueOf(30.5);

	public static final String OLD = "OLD";
	public static final String NEW = "NEW";
	public static final String PLAINS = "PLAINS";
	public static final String HILLS = "HILLS";
	public static final String OLD_AREA_ERROR = "road width old area";
	public static final String NEW_AREA_ERROR = "road width new area";
	public static final String OLD_AREA_ERROR_MSG = "No construction shall be permitted if the road width is less than 2.4m for old area.";
	public static final String NEW_AREA_ERROR_MSG = "No construction shall be permitted if the road width is less than 6.1m for new area.";

	@Override
	public Plan validate(Plan pl) {
		if (pl.getPlot() == null || (pl.getPlot() != null
				&& (pl.getPlot().getArea() == null || pl.getPlot().getArea().doubleValue() == 0))) {
			pl.addError(PLOT_AREA, getLocaleMessage(OBJECTNOTDEFINED, PLOT_AREA));
		}
		return pl;
	}

	@Override
	public Plan process(Plan pl) {
//		decideNocIsRequired(pl); is it required?
		try {
			HashMap<String, String> errorMsgs = new HashMap<>();
			int errors = pl.getErrors().size();
			validate(pl);
			int validatedErrors = pl.getErrors().size();
			if (validatedErrors > errors) {
				return pl;
			}
			BigDecimal totalExistingBuiltUpArea = BigDecimal.ZERO;
			BigDecimal totalExistingFloorArea = BigDecimal.ZERO;
			BigDecimal totalBuiltUpArea = BigDecimal.ZERO;
			BigDecimal totalFloorArea = BigDecimal.ZERO;
			BigDecimal totalCarpetArea = BigDecimal.ZERO;
			BigDecimal totalExistingCarpetArea = BigDecimal.ZERO;
			Set<OccupancyTypeHelper> distinctOccupancyTypesHelper = new HashSet<>();
			for (Block blk : pl.getBlocks()) {
				BigDecimal flrArea = BigDecimal.ZERO;
				BigDecimal bltUpArea = BigDecimal.ZERO;
				BigDecimal existingFlrArea = BigDecimal.ZERO;
				BigDecimal existingBltUpArea = BigDecimal.ZERO;
				BigDecimal carpetArea = BigDecimal.ZERO;
				BigDecimal existingCarpetArea = BigDecimal.ZERO;
				Building building = blk.getBuilding();
				for (Floor flr : building.getFloors()) {

					for (Occupancy occupancy : flr.getOccupancies()) {

						validate2(pl, blk, flr, occupancy);
						bltUpArea = bltUpArea.add(occupancy.getBuiltUpArea() == null ? BigDecimal.valueOf(0)
								: occupancy.getBuiltUpArea());
						existingBltUpArea = existingBltUpArea
								.add(occupancy.getExistingBuiltUpArea() == null ? BigDecimal.valueOf(0)
										: occupancy.getExistingBuiltUpArea());
						flrArea = flrArea.add(occupancy.getFloorArea());
						existingFlrArea = existingFlrArea.add(occupancy.getExistingFloorArea());
						carpetArea = carpetArea.add(occupancy.getCarpetArea());
						existingCarpetArea = existingCarpetArea.add(occupancy.getExistingCarpetArea());
					}
				}
				/*
				 * This is hard coded for testing
				 */
				building.setTotalFloorArea(flrArea);
				building.setTotalBuitUpArea(bltUpArea);
				building.setTotalExistingBuiltUpArea(existingBltUpArea);
				building.setTotalExistingFloorArea(existingFlrArea);

				// check block is completely existing building or not.
				if (existingBltUpArea.compareTo(bltUpArea) == 0)
					blk.setCompletelyExisting(Boolean.TRUE);

				totalFloorArea = totalFloorArea.add(flrArea);
				totalBuiltUpArea = totalBuiltUpArea.add(bltUpArea);
				totalExistingBuiltUpArea = totalExistingBuiltUpArea.add(existingBltUpArea);
				totalExistingFloorArea = totalExistingFloorArea.add(existingFlrArea);
				totalCarpetArea = totalCarpetArea.add(carpetArea);
				totalExistingCarpetArea = totalExistingCarpetArea.add(existingCarpetArea);

				// Find Occupancies by block and add
				Set<OccupancyTypeHelper> occupancyByBlock = new HashSet<>();
				for (Floor flr : building.getFloors()) {
					for (Occupancy occupancy : flr.getOccupancies()) {
						if (occupancy.getTypeHelper().getType() != null)
							occupancyByBlock.add(occupancy.getTypeHelper());
					}
				}

				List<Map<String, Object>> listOfMapOfAllDtls = new ArrayList<>();
				List<OccupancyTypeHelper> listOfOccupancyTypes = new ArrayList<>();

				for (OccupancyTypeHelper occupancyType : occupancyByBlock) {

					Map<String, Object> allDtlsMap = new HashMap<>();
					BigDecimal blockWiseFloorArea = BigDecimal.ZERO;
					BigDecimal blockWiseBuiltupArea = BigDecimal.ZERO;
					BigDecimal blockWiseExistingFloorArea = BigDecimal.ZERO;
					BigDecimal blockWiseExistingBuiltupArea = BigDecimal.ZERO;
					for (Floor flr : blk.getBuilding().getFloors()) {
						for (Occupancy occupancy : flr.getOccupancies()) {
							if (occupancyType.getType() != null && occupancy.getTypeHelper().getType() != null
									&& occupancy.getTypeHelper().getType().getCode()
											.equals(occupancyType.getType().getCode())) {
								blockWiseFloorArea = blockWiseFloorArea.add(occupancy.getFloorArea());
								blockWiseBuiltupArea = blockWiseBuiltupArea
										.add(occupancy.getBuiltUpArea() == null ? BigDecimal.valueOf(0)
												: occupancy.getBuiltUpArea());
								blockWiseExistingFloorArea = blockWiseExistingFloorArea
										.add(occupancy.getExistingFloorArea());
								blockWiseExistingBuiltupArea = blockWiseExistingBuiltupArea
										.add(occupancy.getExistingBuiltUpArea() == null ? BigDecimal.valueOf(0)
												: occupancy.getExistingBuiltUpArea());

							}
						}
					}
					Occupancy occupancy = new Occupancy();
					occupancy.setBuiltUpArea(blockWiseBuiltupArea);
					occupancy.setFloorArea(blockWiseFloorArea);
					occupancy.setExistingFloorArea(blockWiseExistingFloorArea);
					occupancy.setExistingBuiltUpArea(blockWiseExistingBuiltupArea);
					occupancy.setCarpetArea(blockWiseFloorArea.multiply(BigDecimal.valueOf(.80)));
					occupancy.setTypeHelper(occupancyType);
					building.getTotalArea().add(occupancy);

					allDtlsMap.put("occupancy", occupancyType);
					allDtlsMap.put("totalFloorArea", blockWiseFloorArea);
					allDtlsMap.put("totalBuiltUpArea", blockWiseBuiltupArea);
					allDtlsMap.put("existingFloorArea", blockWiseExistingFloorArea);
					allDtlsMap.put("existingBuiltUpArea", blockWiseExistingBuiltupArea);

					listOfOccupancyTypes.add(occupancyType);

					listOfMapOfAllDtls.add(allDtlsMap);
				}
				Set<OccupancyTypeHelper> setOfOccupancyTypes = new HashSet<>(listOfOccupancyTypes);

				List<Occupancy> listOfOccupanciesOfAParticularblock = new ArrayList<>();
				for (OccupancyTypeHelper occupancyType : setOfOccupancyTypes) {
					if (occupancyType != null) {
						Occupancy occupancy = new Occupancy();
						BigDecimal totalFlrArea = BigDecimal.ZERO;
						BigDecimal totalBltUpArea = BigDecimal.ZERO;
						BigDecimal totalExistingFlrArea = BigDecimal.ZERO;
						BigDecimal totalExistingBltUpArea = BigDecimal.ZERO;

						for (Map<String, Object> dtlsMap : listOfMapOfAllDtls) {
							if (occupancyType.equals(dtlsMap.get("occupancy"))) {
								totalFlrArea = totalFlrArea.add((BigDecimal) dtlsMap.get("totalFloorArea"));
								totalBltUpArea = totalBltUpArea.add((BigDecimal) dtlsMap.get("totalBuiltUpArea"));

								totalExistingBltUpArea = totalExistingBltUpArea
										.add((BigDecimal) dtlsMap.get("existingBuiltUpArea"));
								totalExistingFlrArea = totalExistingFlrArea
										.add((BigDecimal) dtlsMap.get("existingFloorArea"));

							}
						}
						occupancy.setTypeHelper(occupancyType);
						occupancy.setBuiltUpArea(totalBltUpArea);
						occupancy.setFloorArea(totalFlrArea);
						occupancy.setExistingBuiltUpArea(totalExistingBltUpArea);
						occupancy.setExistingFloorArea(totalExistingFlrArea);
						occupancy.setExistingCarpetArea(totalExistingFlrArea.multiply(BigDecimal.valueOf(0.80)));
						occupancy.setCarpetArea(totalFlrArea.multiply(BigDecimal.valueOf(0.80)));

						listOfOccupanciesOfAParticularblock.add(occupancy);
					}
				}
				blk.getBuilding().setOccupancies(listOfOccupanciesOfAParticularblock);

				if (!listOfOccupanciesOfAParticularblock.isEmpty()) {
					// listOfOccupanciesOfAParticularblock already converted. In
					// case of professional building type, converted into A1
					// type
					boolean singleFamilyBuildingTypeOccupancyPresent = false;
					boolean otherThanSingleFamilyOccupancyTypePresent = false;

					for (Occupancy occupancy : listOfOccupanciesOfAParticularblock) {
						if (occupancy.getTypeHelper().getSubtype() != null
								&& R.equals(occupancy.getTypeHelper().getType().getCode()))
							singleFamilyBuildingTypeOccupancyPresent = true;
						else {
							otherThanSingleFamilyOccupancyTypePresent = true;
							break;
						}
					}
					blk.setSingleFamilyBuilding(
							!otherThanSingleFamilyOccupancyTypePresent && singleFamilyBuildingTypeOccupancyPresent);
					int allResidentialOccTypes = 0;
					int allResidentialOrCommercialOccTypes = 0;

					for (Occupancy occupancy : listOfOccupanciesOfAParticularblock) {
						if (occupancy.getTypeHelper().getType() != null) {
							// setting residentialBuilding
							int residentialOccupancyType = 0;
							if (R.equals(occupancy.getTypeHelper().getType().getCode())) {
								residentialOccupancyType = 1;
							}
							if (residentialOccupancyType == 0) {
								allResidentialOccTypes = 0;
								break;
							} else {
								allResidentialOccTypes = 1;
							}
						}
					}
					blk.setResidentialBuilding(allResidentialOccTypes == 1);
					for (Occupancy occupancy : listOfOccupanciesOfAParticularblock) {
						if (occupancy.getTypeHelper().getType() != null) {
							// setting residentialOrCommercial Occupancy Type
							int residentialOrCommercialOccupancyType = 0;
							if (R.equals(occupancy.getTypeHelper().getType().getCode())
									|| C.equals(occupancy.getTypeHelper().getType().getCode())) {
								residentialOrCommercialOccupancyType = 1;
							}
							if (residentialOrCommercialOccupancyType == 0) {
								allResidentialOrCommercialOccTypes = 0;
								break;
							} else {
								allResidentialOrCommercialOccTypes = 1;
							}
						}
					}
					blk.setResidentialOrCommercialBuilding(allResidentialOrCommercialOccTypes == 1);
				}

				if (blk.getBuilding().getFloors() != null && !blk.getBuilding().getFloors().isEmpty()) {
					BigDecimal noOfFloorsAboveGround = BigDecimal.ZERO;
					for (Floor floor : blk.getBuilding().getFloors()) {
						if (floor.getNumber() != null && floor.getNumber() >= 0) {
							noOfFloorsAboveGround = noOfFloorsAboveGround.add(BigDecimal.valueOf(1));
						}
					}

					boolean hasTerrace = blk.getBuilding().getFloors().stream()
							.anyMatch(floor -> floor.getTerrace().equals(Boolean.TRUE));

					noOfFloorsAboveGround = hasTerrace ? noOfFloorsAboveGround.subtract(BigDecimal.ONE)
							: noOfFloorsAboveGround;

					blk.getBuilding().setMaxFloor(noOfFloorsAboveGround);
					blk.getBuilding().setFloorsAboveGround(noOfFloorsAboveGround);
					blk.getBuilding().setTotalFloors(BigDecimal.valueOf(blk.getBuilding().getFloors().size()));
				}

			}
			// end of setting block and floor

			// begin get of block and floor
			for (Block blk : pl.getBlocks()) {
				Building building = blk.getBuilding();
				List<OccupancyTypeHelper> blockWiseOccupancyTypes = new ArrayList<>();
				for (Occupancy occupancy : blk.getBuilding().getOccupancies()) {
					if (occupancy.getTypeHelper().getType() != null)
						blockWiseOccupancyTypes.add(occupancy.getTypeHelper());
				}
				Set<OccupancyTypeHelper> setOfBlockDistinctOccupancyTypes = new HashSet<>(blockWiseOccupancyTypes);
				OccupancyTypeHelper mostRestrictiveFar = getMostRestrictiveFar(setOfBlockDistinctOccupancyTypes);
				blk.getBuilding().setMostRestrictiveFarHelper(mostRestrictiveFar);

				for (Floor flr : building.getFloors()) {
					BigDecimal flrArea = BigDecimal.ZERO;
					BigDecimal existingFlrArea = BigDecimal.ZERO;
					BigDecimal carpetArea = BigDecimal.ZERO;
					BigDecimal existingCarpetArea = BigDecimal.ZERO;
					BigDecimal existingBltUpArea = BigDecimal.ZERO;
					for (Occupancy occupancy : flr.getOccupancies()) {
						flrArea = flrArea.add(occupancy.getFloorArea());
						existingFlrArea = existingFlrArea.add(occupancy.getExistingFloorArea());
						carpetArea = carpetArea.add(occupancy.getCarpetArea());
						existingCarpetArea = existingCarpetArea.add(occupancy.getExistingCarpetArea());
					}

					List<Occupancy> occupancies = flr.getOccupancies();
					for (Occupancy occupancy : occupancies) {
						existingBltUpArea = existingBltUpArea
								.add(occupancy.getExistingBuiltUpArea() != null ? occupancy.getExistingBuiltUpArea()
										: BigDecimal.ZERO);
					}

					if (mostRestrictiveFar != null && mostRestrictiveFar.getConvertedSubtype() != null
							&& !R.equals(mostRestrictiveFar.getType().getCode())) {
						if (carpetArea.compareTo(BigDecimal.ZERO) == 0) {
							pl.addError("Carpet area in block " + blk.getNumber() + "floor " + flr.getNumber(),
									"Carpet area is not defined in block " + blk.getNumber() + "floor "
											+ flr.getNumber());
						}

						if (existingBltUpArea.compareTo(BigDecimal.ZERO) > 0
								&& existingCarpetArea.compareTo(BigDecimal.ZERO) == 0) {
							pl.addError("Existing Carpet area in block " + blk.getNumber() + "floor " + flr.getNumber(),
									"Existing Carpet area is not defined in block " + blk.getNumber() + "floor "
											+ flr.getNumber());
						}
					}

					if (flrArea.setScale(DcrConstants.DECIMALDIGITS_MEASUREMENTS, DcrConstants.ROUNDMODE_MEASUREMENTS)
							.compareTo(carpetArea.setScale(DcrConstants.DECIMALDIGITS_MEASUREMENTS,
									DcrConstants.ROUNDMODE_MEASUREMENTS)) < 0) {
						pl.addError("Floor area in block " + blk.getNumber() + "floor " + flr.getNumber(),
								"Floor area is less than carpet area in block " + blk.getNumber() + "floor "
										+ flr.getNumber());
					}

					if (existingBltUpArea.compareTo(BigDecimal.ZERO) > 0 && existingFlrArea
							.setScale(DcrConstants.DECIMALDIGITS_MEASUREMENTS, DcrConstants.ROUNDMODE_MEASUREMENTS)
							.compareTo(existingCarpetArea.setScale(DcrConstants.DECIMALDIGITS_MEASUREMENTS,
									DcrConstants.ROUNDMODE_MEASUREMENTS)) < 0) {
						pl.addError("Existing floor area in block " + blk.getNumber() + "floor " + flr.getNumber(),
								"Existing Floor area is less than carpet area in block " + blk.getNumber() + "floor "
										+ flr.getNumber());
					}
				}
			}

			List<OccupancyTypeHelper> plotWiseOccupancyTypes = new ArrayList<>();
			for (Block block : pl.getBlocks()) {
				for (Occupancy occupancy : block.getBuilding().getOccupancies()) {
					if (occupancy.getTypeHelper().getType() != null)
						plotWiseOccupancyTypes.add(occupancy.getTypeHelper());
				}
			}

			Set<OccupancyTypeHelper> setOfDistinctOccupancyTypes = new HashSet<>(plotWiseOccupancyTypes);

			distinctOccupancyTypesHelper.addAll(setOfDistinctOccupancyTypes);

			List<Occupancy> occupanciesForPlan = new ArrayList<>();

			for (OccupancyTypeHelper occupancyType : setOfDistinctOccupancyTypes) {
				if (occupancyType != null) {
					BigDecimal totalFloorAreaForAllBlks = BigDecimal.ZERO;
					BigDecimal totalBuiltUpAreaForAllBlks = BigDecimal.ZERO;
					BigDecimal totalCarpetAreaForAllBlks = BigDecimal.ZERO;
					BigDecimal totalExistBuiltUpAreaForAllBlks = BigDecimal.ZERO;
					BigDecimal totalExistFloorAreaForAllBlks = BigDecimal.ZERO;
					BigDecimal totalExistCarpetAreaForAllBlks = BigDecimal.ZERO;
					Occupancy occupancy = new Occupancy();
					for (Block block : pl.getBlocks()) {
						for (Occupancy buildingOccupancy : block.getBuilding().getOccupancies()) {
							if (occupancyType.equals(buildingOccupancy.getTypeHelper())) {
								totalFloorAreaForAllBlks = totalFloorAreaForAllBlks
										.add(buildingOccupancy.getFloorArea());
								totalBuiltUpAreaForAllBlks = totalBuiltUpAreaForAllBlks
										.add(buildingOccupancy.getBuiltUpArea());
								totalCarpetAreaForAllBlks = totalCarpetAreaForAllBlks
										.add(buildingOccupancy.getCarpetArea());
								totalExistBuiltUpAreaForAllBlks = totalExistBuiltUpAreaForAllBlks
										.add(buildingOccupancy.getExistingBuiltUpArea());
								totalExistFloorAreaForAllBlks = totalExistFloorAreaForAllBlks
										.add(buildingOccupancy.getExistingFloorArea());
								totalExistCarpetAreaForAllBlks = totalExistCarpetAreaForAllBlks
										.add(buildingOccupancy.getExistingCarpetArea());
							}
						}
					}
					occupancy.setTypeHelper(occupancyType);
					occupancy.setBuiltUpArea(totalBuiltUpAreaForAllBlks);
					occupancy.setCarpetArea(totalCarpetAreaForAllBlks);
					occupancy.setFloorArea(totalFloorAreaForAllBlks);
					occupancy.setExistingBuiltUpArea(totalExistBuiltUpAreaForAllBlks);
					occupancy.setExistingFloorArea(totalExistFloorAreaForAllBlks);
					occupancy.setExistingCarpetArea(totalExistCarpetAreaForAllBlks);
					occupanciesForPlan.add(occupancy);
				}
			}

			pl.setOccupancies(occupanciesForPlan);
			pl.getVirtualBuilding().setTotalFloorArea(totalFloorArea);
			pl.getVirtualBuilding().setTotalCarpetArea(totalCarpetArea);
			pl.getVirtualBuilding().setTotalExistingBuiltUpArea(totalExistingBuiltUpArea);
			pl.getVirtualBuilding().setTotalExistingFloorArea(totalExistingFloorArea);
			pl.getVirtualBuilding().setTotalExistingCarpetArea(totalExistingCarpetArea);
			pl.getVirtualBuilding().setOccupancyTypes(distinctOccupancyTypesHelper);
			pl.getVirtualBuilding().setTotalBuitUpArea(totalBuiltUpArea);
			pl.getVirtualBuilding().setMostRestrictiveFarHelper(getMostRestrictiveFar(setOfDistinctOccupancyTypes));

			if (!distinctOccupancyTypesHelper.isEmpty()) {
				int allResidentialOccTypesForPlan = 0;
				for (OccupancyTypeHelper occupancy : distinctOccupancyTypesHelper) {
					// setting residentialBuilding
					int residentialOccupancyType = 0;
					if (R.equals(occupancy.getType().getCode())) {
						residentialOccupancyType = 1;
					}
					if (residentialOccupancyType == 0) {
						allResidentialOccTypesForPlan = 0;
						break;
					} else {
						allResidentialOccTypesForPlan = 1;
					}
				}
				pl.getVirtualBuilding().setResidentialBuilding(allResidentialOccTypesForPlan == 1);
				int allResidentialOrCommercialOccTypesForPlan = 0;
				for (OccupancyTypeHelper occupancyType : distinctOccupancyTypesHelper) {
					int residentialOrCommercialOccupancyTypeForPlan = 0;
					if (R.equals(occupancyType.getType().getCode()) || C.equals(occupancyType.getType().getCode())) {
						residentialOrCommercialOccupancyTypeForPlan = 1;
					}
					if (residentialOrCommercialOccupancyTypeForPlan == 0) {
						allResidentialOrCommercialOccTypesForPlan = 0;
						break;
					} else {
						allResidentialOrCommercialOccTypesForPlan = 1;
					}
				}
				pl.getVirtualBuilding()
						.setResidentialOrCommercialBuilding(allResidentialOrCommercialOccTypesForPlan == 1);
			}
			if (!pl.getVirtualBuilding().getResidentialOrCommercialBuilding()) {
				pl.getErrors().put(DxfFileConstants.OCCUPANCY_ALLOWED_KEY, DxfFileConstants.OCCUPANCY_ALLOWED);
				return pl;
			}
			OccupancyTypeHelper mostRestrictiveOccupancyType = pl.getVirtualBuilding().getMostRestrictiveFarHelper();
			BigDecimal providedFar = BigDecimal.ZERO;
			BigDecimal surrenderRoadArea = BigDecimal.ZERO;

			if (!pl.getSurrenderRoads().isEmpty()) {
				for (Measurement measurement : pl.getSurrenderRoads()) {
					surrenderRoadArea = surrenderRoadArea.add(measurement.getArea());
				}
			}

			pl.setTotalSurrenderRoadArea(surrenderRoadArea.setScale(DcrConstants.DECIMALDIGITS_MEASUREMENTS,
					DcrConstants.ROUNDMODE_MEASUREMENTS));
			BigDecimal plotArea = pl.getPlot() != null ? pl.getPlot().getArea().add(surrenderRoadArea)
					: BigDecimal.ZERO;
			if (plotArea.doubleValue() > 0)
				providedFar = pl.getVirtualBuilding().getTotalFloorArea().divide(plotArea, DECIMALDIGITS_MEASUREMENTS,
						ROUNDMODE_MEASUREMENTS); // Calculation for FAR
			pl.setFarDetails(new FarDetails());
			pl.getFarDetails().setProvidedFar(providedFar.doubleValue());
			BigDecimal PlotArea = pl.getPlot().getArea();
			if (mostRestrictiveOccupancyType != null) {
				if ((mostRestrictiveOccupancyType.getType() != null
						&& R.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode()))) {
					processFarResidential(pl, mostRestrictiveOccupancyType, providedFar, PlotArea, errorMsgs);
				}
				if (mostRestrictiveOccupancyType.getType() != null
						&& C.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {
					processFarCommercial(pl, mostRestrictiveOccupancyType, providedFar, PlotArea, errorMsgs);
				}
				if (mostRestrictiveOccupancyType.getType() != null
						&& I.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

					processFarIndustrial(pl, mostRestrictiveOccupancyType, providedFar, PlotArea, errorMsgs);
				}
				if (mostRestrictiveOccupancyType.getType() != null
						&& G.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

					processFarGovernmentUse(pl, mostRestrictiveOccupancyType, providedFar, PlotArea, errorMsgs);
				}
				if (mostRestrictiveOccupancyType.getType() != null
						&& T.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

					processFarTransportation(pl, mostRestrictiveOccupancyType, providedFar, PlotArea, errorMsgs);
				}
				if (mostRestrictiveOccupancyType.getType() != null
						&& P.equalsIgnoreCase(mostRestrictiveOccupancyType.getType().getCode())) {

					processFarPublicSemiPublic(pl, mostRestrictiveOccupancyType, providedFar, PlotArea, errorMsgs);
				}

			}

			ProcessPrintHelper.print(pl);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return pl;
	}

	private void validate2(Plan pl, Block blk, Floor flr, Occupancy occupancy) {
		String occupancyTypeHelper = StringUtils.EMPTY;
		if (occupancy.getTypeHelper() != null) {
			if (occupancy.getTypeHelper().getType() != null) {
				occupancyTypeHelper = occupancy.getTypeHelper().getType().getName();
			} else if (occupancy.getTypeHelper().getSubtype() != null) {
				occupancyTypeHelper = occupancy.getTypeHelper().getSubtype().getName();
			}
		}

		if (occupancy.getBuiltUpArea() != null && occupancy.getBuiltUpArea().compareTo(BigDecimal.valueOf(0)) < 0) {
			pl.addError(VALIDATION_NEGATIVE_BUILTUP_AREA, getLocaleMessage(VALIDATION_NEGATIVE_BUILTUP_AREA,
					blk.getNumber(), flr.getNumber().toString(), occupancyTypeHelper));
		}
		if (occupancy.getExistingBuiltUpArea() != null
				&& occupancy.getExistingBuiltUpArea().compareTo(BigDecimal.valueOf(0)) < 0) {
			pl.addError(VALIDATION_NEGATIVE_EXISTING_BUILTUP_AREA,
					getLocaleMessage(VALIDATION_NEGATIVE_EXISTING_BUILTUP_AREA, blk.getNumber(),
							flr.getNumber().toString(), occupancyTypeHelper));
		}
		occupancy.setFloorArea((occupancy.getBuiltUpArea() == null ? BigDecimal.ZERO : occupancy.getBuiltUpArea())
				.subtract(occupancy.getDeduction() == null ? BigDecimal.ZERO : occupancy.getDeduction()));
		if (occupancy.getFloorArea() != null && occupancy.getFloorArea().compareTo(BigDecimal.valueOf(0)) < 0) {
			pl.addError(VALIDATION_NEGATIVE_FLOOR_AREA, getLocaleMessage(VALIDATION_NEGATIVE_FLOOR_AREA,
					blk.getNumber(), flr.getNumber().toString(), occupancyTypeHelper));
		}
		occupancy.setExistingFloorArea(
				(occupancy.getExistingBuiltUpArea() == null ? BigDecimal.ZERO : occupancy.getExistingBuiltUpArea())
						.subtract(occupancy.getExistingDeduction() == null ? BigDecimal.ZERO
								: occupancy.getExistingDeduction()));
		if (occupancy.getExistingFloorArea() != null
				&& occupancy.getExistingFloorArea().compareTo(BigDecimal.valueOf(0)) < 0) {
			pl.addError(VALIDATION_NEGATIVE_EXISTING_FLOOR_AREA,
					getLocaleMessage(VALIDATION_NEGATIVE_EXISTING_FLOOR_AREA, blk.getNumber(),
							flr.getNumber().toString(), occupancyTypeHelper));
		}
	}

	private void processFarResidential(Plan pl, OccupancyTypeHelper occupancyType, BigDecimal far, BigDecimal PlotArea,
			HashMap<String, String> errors) {
		OccupancyTypeHelper mostRestrictiveOccupancyType = pl.getVirtualBuilding() != null
				? pl.getVirtualBuilding().getMostRestrictiveFarHelper()
				: null;

		String expectedResult = StringUtils.EMPTY;
		boolean isAccepted = false;
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(R1a)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(R1b)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(R1c)) {
			if (PlotArea.compareTo(PLOT_AREA_48) < 0) {
				isAccepted = far.compareTo(ONE_POINTFIVE) <= 0;
				pl.getFarDetails().setPermissableFar(ONE_POINTFIVE.doubleValue());
				expectedResult = "<= 1.5";
			} else if (PlotArea.compareTo(PLOT_AREA_48) >= 0 && PlotArea.compareTo(PLOT_AREA_60) < 0) {
				isAccepted = far.compareTo(ONE_POINTFIVE) <= 0;
				pl.getFarDetails().setPermissableFar(ONE_POINTFIVE.doubleValue());
				expectedResult = "<= 1.5";
			} else if (PlotArea.compareTo(PLOT_AREA_60) >= 0 && PlotArea.compareTo(PLOT_AREA_100) < 0) {
				isAccepted = far.compareTo(ONE_POINTEIGHT) <= 0;
				pl.getFarDetails().setPermissableFar(ONE_POINTEIGHT.doubleValue());
				expectedResult = "<= 1.8";
			} else if (PlotArea.compareTo(PLOT_AREA_100) >= 0 && PlotArea.compareTo(PLOT_AREA_250) < 0) {
				isAccepted = far.compareTo(ONE_POINTEIGHT) <= 0;
				pl.getFarDetails().setPermissableFar(ONE_POINTEIGHT.doubleValue());
				expectedResult = "<= 1.8";
			} else if (PlotArea.compareTo(PLOT_AREA_250) >= 0 && PlotArea.compareTo(PLOT_AREA_500) < 0) {
				isAccepted = far.compareTo(TWO) <= 0;
				pl.getFarDetails().setPermissableFar(TWO.doubleValue());
				expectedResult = "<= 2.0";
			} else if (PlotArea.compareTo(PLOT_AREA_500) >= 0 && PlotArea.compareTo(PLOT_AREA_1000) < 0) {
				isAccepted = far.compareTo(TWO_POINTFIVE) <= 0;
				pl.getFarDetails().setPermissableFar(TWO_POINTFIVE.doubleValue());
				expectedResult = "<= 2.5";
			} else if (PlotArea.compareTo(PLOT_AREA_1000) >= 0 && PlotArea.compareTo(PLOT_AREA_1500) < 0) {
				isAccepted = far.compareTo(TWO_POINTFIVE) <= 0;
				pl.getFarDetails().setPermissableFar(TWO_POINTFIVE.doubleValue());
				expectedResult = "<= 2.5";
			} else if (PlotArea.compareTo(PLOT_AREA_1500) >= 0 && PlotArea.compareTo(PLOT_AREA_3000) < 0) {
				isAccepted = far.compareTo(TWO_TWENTYFIVE) <= 0;
				pl.getFarDetails().setPermissableFar(TWO_TWENTYFIVE.doubleValue());
				expectedResult = "<= 2.25";

			}else if (PlotArea.compareTo(PLOT_AREA_3000) > 0) {
				errors.put("PlotArea Above 3000 Residential","Plot area should not exceed above 3000 sqmts for plotted residential housing");
				pl.addErrors(errors);
			}

		}

		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(R2a)) {
			if(PlotArea.compareTo(PLOT_AREA_10000)<0) {
				errors.put("PlotArea below 10000 R2a","Plot area below 10000 sqmts not allowed for Farm House");
				pl.addErrors(errors);
			}
			else if (PlotArea.compareTo(PLOT_AREA_10000) >= 0 && PlotArea.compareTo(PLOT_AREA_20000) < 0) {
				isAccepted = far.compareTo(ONE) <= 0;
				pl.getFarDetails().setPermissableFar(ONE.doubleValue());
				expectedResult = "<= 1";

			} else if (PlotArea.compareTo(PLOT_AREA_20000) >= 0) {
				isAccepted = far.compareTo(ONE_POINTFIVE) <= 0;
				pl.getFarDetails().setPermissableFar(ONE_POINTFIVE.doubleValue());
				expectedResult = "<= 1.5";

			}

		}

		if (errors.isEmpty() && StringUtils.isNotBlank(expectedResult)) {
			buildResult(pl, occupancyType, far, expectedResult, isAccepted);
		}

	}

	private void processFarGovernmentUse(Plan pl, OccupancyTypeHelper occupancyType, BigDecimal far,
			BigDecimal PlotArea, HashMap<String, String> errors) {
		OccupancyTypeHelper mostRestrictiveOccupancyType = pl.getVirtualBuilding() != null
				? pl.getVirtualBuilding().getMostRestrictiveFarHelper()
				: null;

		String expectedResult = StringUtils.EMPTY;
		boolean isAccepted = false;
		// General/Govt./ Integrated Office Complex
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(G2a)) {
			isAccepted = far.compareTo(TWO) <= 0;
			pl.getFarDetails().setPermissableFar(TWO.doubleValue());
			expectedResult = "<= 2.00";

		}
		// District Court
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(G1a)) {
			isAccepted = far.compareTo(TWO) <= 0;
			pl.getFarDetails().setPermissableFar(TWO.doubleValue());
			expectedResult = "<= 2.00";

		}
		// Police Post
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(G3a)) {
			isAccepted = far.compareTo(ONE_POINTTWO) <= 0;
			pl.getFarDetails().setPermissableFar(ONE_POINTTWO.doubleValue());
			expectedResult = "<= 1.2";
		}
		// Police Station
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(G3b)) {
			isAccepted = far.compareTo(ONE_POINTTWO) <= 0;
			pl.getFarDetails().setPermissableFar(ONE_POINTTWO.doubleValue());
			expectedResult = "<= 1.2";
		}
		// Other safety departments,Disaster manangement center, Fire Station etc.
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(G3c)) {
			isAccepted = far.compareTo(ONE_POINTTWO) < 0;
			pl.getFarDetails().setPermissableFar(ONE_POINTTWO.doubleValue());
			expectedResult = "<= 1.2";

		}

		if (errors.isEmpty() && StringUtils.isNotBlank(expectedResult)) {
			buildResult(pl, occupancyType, far, expectedResult, isAccepted);
		}
	}

	private void processFarPublicSemiPublic(Plan pl, OccupancyTypeHelper occupancyType, BigDecimal far,
			BigDecimal PlotArea, HashMap<String, String> errors) {
		OccupancyTypeHelper mostRestrictiveOccupancyType = pl.getVirtualBuilding() != null
				? pl.getVirtualBuilding().getMostRestrictiveFarHelper()
				: null;

		String expectedResult = StringUtils.EMPTY;
		boolean isAccepted = false;

		// Meeting Hall, Auditorium, Community Center
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P3a)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(P3b)) {
			isAccepted = far.compareTo(ONE_POINTTWO) <= 0;
			pl.getFarDetails().setPermissableFar(ONE_POINTTWO.doubleValue());
			expectedResult = "<= 1.2";

		}
		
		//Hostel
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P3c)) {
			if(PlotArea.compareTo(PLOT_AREA_100)<0) {
				errors.put("PlotArea below 100 Hostel","Plot area below 100 sqmts not allowed for hostel");
				pl.addErrors(errors);
			}
			else if (PlotArea.compareTo(PLOT_AREA_100) > 0 && PlotArea.compareTo(PLOT_AREA_250) <= 0) {
				isAccepted = far.compareTo(POINTEIGHT) <= 0;
				pl.getFarDetails().setPermissableFar(POINTEIGHT.doubleValue());
				expectedResult = "<= 1.8";
			} else if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
				isAccepted = far.compareTo(TWO) <= 0;
				pl.getFarDetails().setPermissableFar(TWO.doubleValue());
				expectedResult = "<= 2.0";

			} else if (PlotArea.compareTo(PLOT_AREA_500) > 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
				isAccepted = far.compareTo(TWO_POINTFIVE) <= 0;
				pl.getFarDetails().setPermissableFar(TWO_POINTFIVE.doubleValue());
				expectedResult = "<= 2.5";
			} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
				isAccepted = far.compareTo(TWO_POINTFIVE) <= 0;
				pl.getFarDetails().setPermissableFar(TWO_POINTFIVE.doubleValue());
				expectedResult = "<= 2.5";
			} else if (PlotArea.compareTo(PLOT_AREA_1500) > 0 && PlotArea.compareTo(PLOT_AREA_3000) <= 0) {
				isAccepted = far.compareTo(TWO_POINTTWOFIVE) <= 0;
				pl.getFarDetails().setPermissableFar(TWO_POINTTWOFIVE.doubleValue());
				expectedResult = "<= 2.25";
			}else if(PlotArea.compareTo(PLOT_AREA_3000)>0) {
				errors.put("PlotArea Above 3000 hostel","Plot area should not exceed above 3000 sqmts for hostel");
				pl.addErrors(errors);
			}

		}
		// Hospital
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P2a)) {
			isAccepted = far.compareTo(TWO_POINTFIVE) <= 0;
			pl.getFarDetails().setPermissableFar(TWO_POINTFIVE.doubleValue());
			expectedResult = "<= 2.5";

		}

		// Nursing Center
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P2b)) {
			isAccepted = far.compareTo(ONE_POINTFIVE) <= 0;
			pl.getFarDetails().setPermissableFar(ONE_POINTFIVE.doubleValue());
			expectedResult = "<= 1.5";
		}

		// Veterinary
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P2c)) {
			isAccepted = far.compareTo(ONE_POINTFIVE) <= 0;
			pl.getFarDetails().setPermissableFar(ONE_POINTFIVE.doubleValue());
			expectedResult = "<= 1.5";
		}

		// Medical College, Nursing and paramedic Institute
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P2d)) {
			isAccepted = far.compareTo(ONE) < 0;
			pl.getFarDetails().setPermissableFar(ONE.doubleValue());
			expectedResult = "<= 1.0";
		}

		// Nursery School
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P1b)) {
			isAccepted = far.compareTo(POINTEIGHT) < 0;
			pl.getFarDetails().setPermissableFar(POINTEIGHT.doubleValue());
			expectedResult = "<= 0.8";

		}

		// Primary School
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P1c)) {
			isAccepted = far.compareTo(ONE_POINTSIX) <= 0;
			pl.getFarDetails().setPermissableFar(ONE_POINTSIX.doubleValue());
			expectedResult = "<= 1.6";
		}

		// High / Higher Secondary
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P1d)) {
			isAccepted = far.compareTo(ONE_POINTSIX) <= 0;
			pl.getFarDetails().setPermissableFar(ONE_POINTSIX.doubleValue());
			expectedResult = "<= 1.6";
		}

		// College
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P1e)) {
			isAccepted = far.compareTo(ONE_POINTSEVENFIVE) <= 0;
			pl.getFarDetails().setPermissableFar(ONE_POINTSEVENFIVE.doubleValue());
			expectedResult = "<= 1.75";
		}

		// Special School
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P1g)) {
			isAccepted = far.compareTo(ONE_POINTFIVE) <= 0;
			pl.getFarDetails().setPermissableFar(ONE_POINTFIVE.doubleValue());
			expectedResult = "<= 1.5";
		}

		// Edu & Research Centre
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P1f)) {
			isAccepted = far.compareTo(ONE_POINTTWO) <= 0;
			pl.getFarDetails().setPermissableFar(ONE_POINTTWO.doubleValue());
			expectedResult = "<= 1.2";
		}

		// Sports
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(P1a)) {
			isAccepted = far.compareTo(POINT_FOUR) <= 0;
			pl.getFarDetails().setPermissableFar(POINT_FOUR.doubleValue());
			expectedResult = "<= 0.4";
		}

		if (errors.isEmpty() && StringUtils.isNotBlank(expectedResult)) {
			buildResult(pl, occupancyType, far, expectedResult, isAccepted);
		}

	}

	private void processFarCommercial(Plan pl, OccupancyTypeHelper occupancyType, BigDecimal far, BigDecimal PlotArea,
			HashMap<String, String> errors) {

		OccupancyTypeHelper mostRestrictiveOccupancyType = pl.getVirtualBuilding() != null
				? pl.getVirtualBuilding().getMostRestrictiveFarHelper()
				: null;
		String expectedResult = StringUtils.EMPTY;
		boolean isAccepted = false;
		// Restaurants/shops
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C1a)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(C1b)) {
			if(PlotArea.compareTo(PLOT_AREA_48)<0) {
				errors.put("PlotArea below 48 commmercial","Plot area below 48 sqmts not allowed for Shops/Restaurant");
				pl.addErrors(errors);
			}
			else if (PlotArea.compareTo(PLOT_AREA_48) >= 0 && PlotArea.compareTo(PLOT_AREA_100) <= 0) {
				isAccepted = far.compareTo(ONE_POINTSIX) <= 0;
				pl.getFarDetails().setPermissableFar(ONE_POINTSIX.doubleValue());
				expectedResult = "<= 1.60";
			} else if (PlotArea.compareTo(PLOT_AREA_100) > 0 && PlotArea.compareTo(PLOT_AREA_250) <= 0) {
				isAccepted = far.compareTo(ONE_POINTEIGHT) <= 0;
				pl.getFarDetails().setPermissableFar(ONE_POINTEIGHT.doubleValue());
				expectedResult = "<= 1.80";
			} else if (PlotArea.compareTo(PLOT_AREA_250) > 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
				isAccepted = far.compareTo(TWO) <= 0;
				pl.getFarDetails().setPermissableFar(TWO.doubleValue());
				expectedResult = "<= 2.00";
			} else if (PlotArea.compareTo(PLOT_AREA_500) > 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
				isAccepted = far.compareTo(TWO_POINTFIVE) <= 0;
				pl.getFarDetails().setPermissableFar(TWO_POINTFIVE.doubleValue());
				expectedResult = "<= 2.50";
			} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
				isAccepted = far.compareTo(TWO_POINTFIVE) <= 0;
				pl.getFarDetails().setPermissableFar(TWO_POINTFIVE.doubleValue());
				expectedResult = "<= 2.50";
			} else if (PlotArea.compareTo(PLOT_AREA_1500) > 0 && PlotArea.compareTo(PLOT_AREA_3000) <= 0) {
				isAccepted = far.compareTo(TWO_POINTTWOFIVE) <= 0;
				pl.getFarDetails().setPermissableFar(TWO_POINTTWOFIVE.doubleValue());
				expectedResult = "<= 2.25";
			}else if(PlotArea.compareTo(PLOT_AREA_3000)>0) {
				errors.put("PlotArea Above 3000 Commercial","Plot area should not exceed above 3000 sqmts for Restaurant/Shops");
				pl.addErrors(errors);
			}

		}

		// Hotel
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C2a)) {
			if(PlotArea.compareTo(PLOT_AREA_250)<0){
				errors.put("PlotArea below 250 Hotel","Plot area below 250 sqmts not allowed for hotel");
				pl.addErrors(errors);
			}
			else if (PlotArea.compareTo(PLOT_AREA_250) >= 0 && PlotArea.compareTo(PLOT_AREA_500) <= 0) {
				isAccepted = far.compareTo(TWO_POINTFIVE) <= 0;
				pl.getFarDetails().setPermissableFar(TWO_POINTFIVE.doubleValue());
				expectedResult = "<= 2.50";
			} else if (PlotArea.compareTo(PLOT_AREA_500) >= 0 && PlotArea.compareTo(PLOT_AREA_1000) <= 0) {
				isAccepted = far.compareTo(TWO_POINTFIVE) <= 0;
				pl.getFarDetails().setPermissableFar(TWO_POINTFIVE.doubleValue());
				expectedResult = "<= 2.50";
			} else if (PlotArea.compareTo(PLOT_AREA_1000) > 0 && PlotArea.compareTo(PLOT_AREA_1500) <= 0) {
				isAccepted = far.compareTo(THREE) <= 0;
				pl.getFarDetails().setPermissableFar(THREE.doubleValue());
				expectedResult = "<= 3.0";
			} else if (PlotArea.compareTo(PLOT_AREA_1500) > 0) {
				isAccepted = far.compareTo(THREE) <= 0;
				pl.getFarDetails().setPermissableFar(THREE.doubleValue());
				expectedResult = "<= 3.0";

			}

		}
		// Motel
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C3a)) {
			isAccepted = far.compareTo(TWO) <= 0;
			pl.getFarDetails().setPermissableFar(TWO.doubleValue());
			expectedResult = "<= 2.0";

		}
		// Resort
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C4a)) {
				isAccepted = far.compareTo(ONE_POINTFIVE) <= 0;
				pl.getFarDetails().setPermissableFar(ONE_POINTFIVE.doubleValue());
				expectedResult = "<= 1.5";
		}
		// Petrol Pump
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(C5a)) {
			if (PlotArea.compareTo(PLOT_AREA_1080) > 0) {
				isAccepted = far.compareTo(POINTTWO) <= 0;
				pl.getFarDetails().setPermissableFar(POINTTWO.doubleValue());
				expectedResult = "<= 0.20";

			} else if (PlotArea.compareTo(PLOT_AREA_510) > 0) {
				isAccepted = far.compareTo(POINTONE) <= 0;
				pl.getFarDetails().setPermissableFar(POINTONE.doubleValue());
				expectedResult = "<= 0.10";

			}else {
				errors.put("PlotArea below 510 Petrol","Plot area below 510 sqmts not allowed for Petrol Pump");
				pl.addErrors(errors);
			}

		}
		// Wholesale
		if (mostRestrictiveOccupancyType.getUsage().getCode().equals(C6a)) {

			isAccepted = far.compareTo(EIGHT) <= 0;
			pl.getFarDetails().setPermissableFar(EIGHT.doubleValue());
			expectedResult = "<= 8.00";

		}

		if (errors.isEmpty() && StringUtils.isNotBlank(expectedResult)) {
			buildResult(pl, occupancyType, far, expectedResult, isAccepted);
		}

	}

	private void processFarIndustrial(Plan pl, OccupancyTypeHelper occupancyType, BigDecimal far, BigDecimal PlotArea,
			HashMap<String, String> errors) {

		OccupancyTypeHelper mostRestrictiveOccupancyType = pl.getVirtualBuilding() != null
				? pl.getVirtualBuilding().getMostRestrictiveFarHelper()
				: null;

		String expectedResult = StringUtils.EMPTY;
		boolean isAccepted = false;
		// Flatted
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(I1a)) {
			if(PlotArea.compareTo(PLOT_AREA_2000)<0) {
				errors.put("PlotArea below 2000 Flatted","Plot area below 2000 sqmts not allowed for Flatted Industries");
				pl.addErrors(errors);
			}
			else if (PlotArea.compareTo(PLOT_AREA_2000) >= 0) {
				isAccepted = far.compareTo(ONE_POINTTWO) <= 0;
				pl.getFarDetails().setPermissableFar(ONE_POINTTWO.doubleValue());
				expectedResult = "<= 1.20";
			}

		}

		// Light and Service
		// check for plains and hills
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(I1b)) {
			if ((pl.getPlanInfoProperties().get("TERRAIN")).equals("PLAINS")) {
				if (PlotArea.compareTo(PLOT_AREA_400) <= 0 ) {
					isAccepted = far.compareTo(ONE_POINTTWOFIVE) <= 0;
					pl.getFarDetails().setPermissableFar(ONE_POINTTWOFIVE.doubleValue());
					expectedResult = "<= 1.25";
				}
				if (PlotArea.compareTo(PLOT_AREA_400) > 0 && PlotArea.compareTo(PLOT_AREA_4000) <= 0) {
					isAccepted = far.compareTo(ONE_POINTTWOFIVE) <= 0;
					pl.getFarDetails().setPermissableFar(ONE_POINTTWOFIVE.doubleValue());
					expectedResult = "<= 1.25";
				} else if (PlotArea.compareTo(PLOT_AREA_4000) > 0 && PlotArea.compareTo(PLOT_AREA_12000) <= 0) {
					isAccepted = far.compareTo(ONE_POINTTWOFIVE) <= 0;
					pl.getFarDetails().setPermissableFar(ONE_POINTTWOFIVE.doubleValue());
					expectedResult = "<= 1.25";
				} else if (PlotArea.compareTo(PLOT_AREA_12000) > 0) {
					isAccepted = far.compareTo(POINTONE) <= 0;
					pl.getFarDetails().setPermissableFar(POINTONE.doubleValue());
					expectedResult = "<= 1.00";
				}
			} else if ((pl.getPlanInfoProperties().get("TERRAIN")).equals("HILLS")) {
				if (PlotArea.compareTo(PLOT_AREA_400) <= 0 ) {
					isAccepted = far.compareTo(ONE) <= 0;
					pl.getFarDetails().setPermissableFar(ONE.doubleValue());
					expectedResult = "<= 1.00";
				}
				if (PlotArea.compareTo(PLOT_AREA_400) > 0  && PlotArea.compareTo(PLOT_AREA_4000) <= 0) {
					isAccepted = far.compareTo(ONE) <= 0;
					pl.getFarDetails().setPermissableFar(ONE.doubleValue());
					expectedResult = "<= 1.00";
				} else if (PlotArea.compareTo(PLOT_AREA_4000) > 0 && PlotArea.compareTo(PLOT_AREA_12000) <= 0) {
					isAccepted = far.compareTo(ONE) <= 0;
					pl.getFarDetails().setPermissableFar(ONE.doubleValue());
					expectedResult = "<= 1.00";
				} else if (PlotArea.compareTo(PLOT_AREA_12000) > 0) {
					isAccepted = far.compareTo(POINTSEVENFIVE) <= 0;
					pl.getFarDetails().setPermissableFar(POINTSEVENFIVE.doubleValue());
					expectedResult = "<= 0.75";
				}
			} else {
				pl.addError("TERRAIN TYPE", "Terrain Type should be PLAINS/HILLS");
			}
		}

		if (errors.isEmpty() && StringUtils.isNotBlank(expectedResult)) {
			buildResult(pl, occupancyType, far, expectedResult, isAccepted);
		}

	}

	private void processFarTransportation(Plan pl, OccupancyTypeHelper occupancyType, BigDecimal far,
			BigDecimal PlotArea, HashMap<String, String> errors) {

		OccupancyTypeHelper mostRestrictiveOccupancyType = pl.getVirtualBuilding() != null
				? pl.getVirtualBuilding().getMostRestrictiveFarHelper()
				: null;

		String expectedResult = StringUtils.EMPTY;
		boolean isAccepted = false;
		// Rail Terminal
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(T1b)) {
			isAccepted = far.compareTo(ONE) <= 0;
			pl.getFarDetails().setPermissableFar(ONE.doubleValue());
			expectedResult = "<= 1.0";

		}
		// ISBT Bus Terminal
		if (mostRestrictiveOccupancyType.getSubtype().getCode().equals(T1a)
				|| mostRestrictiveOccupancyType.getSubtype().getCode().equals(T1c)) {
			isAccepted = far.compareTo(ONE) <= 0;
			pl.getFarDetails().setPermissableFar(ONE.doubleValue());
			expectedResult = "<= 1.0";

		}

		if (errors.isEmpty() && StringUtils.isNotBlank(expectedResult)) {
			buildResult(pl, occupancyType, far, expectedResult, isAccepted);
		}

	}

	private void buildResult(Plan pl, OccupancyTypeHelper occupancyType, BigDecimal far, String expectedResult,
			boolean isAccepted) {
		ScrutinyDetail scrutinyDetail = new ScrutinyDetail();
		scrutinyDetail.addColumnHeading(1, RULE_NO);
		scrutinyDetail.addColumnHeading(2, OCCUPANCY);
//		scrutinyDetail.addColumnHeading(3, AREA_TYPE);
//		scrutinyDetail.addColumnHeading(4, ROAD_WIDTH);
		scrutinyDetail.addColumnHeading(5, PERMISSIBLE);
		scrutinyDetail.addColumnHeading(6, PROVIDED);
		scrutinyDetail.addColumnHeading(7, STATUS);
		scrutinyDetail.setKey("Common_FAR");
		String actualResult = far.toString();
		String occupancyName;
		if (occupancyType.getSubtype() != null)
			occupancyName = occupancyType.getSubtype().getName();
		else
			occupancyName = occupancyType.getType().getName();

		Map<String, String> details = new HashMap<>();
		details.put(RULE_NO, RULE_38);
		details.put(OCCUPANCY, occupancyName);
//		 details.put(AREA_TYPE, typeOfArea);
//		 details.put(ROAD_WIDTH, roadWidth.toString());
		details.put(PERMISSIBLE, expectedResult);
		details.put(PROVIDED, actualResult);
		details.put(STATUS, isAccepted ? Result.Accepted.getResultVal() : Result.Not_Accepted.getResultVal());

		scrutinyDetail.getDetail().add(details);
		pl.getReportOutput().getScrutinyDetails().add(scrutinyDetail);
	}

	private ScrutinyDetail getFarScrutinyDetail(String key) {
		ScrutinyDetail scrutinyDetail = new ScrutinyDetail();
		scrutinyDetail.addColumnHeading(1, RULE_NO);
		scrutinyDetail.addColumnHeading(2, "Area Type");
		scrutinyDetail.addColumnHeading(3, "Road Width");
		scrutinyDetail.addColumnHeading(4, PERMISSIBLE);
		scrutinyDetail.addColumnHeading(5, PROVIDED);
		scrutinyDetail.addColumnHeading(6, STATUS);
		scrutinyDetail.setKey(key);
		return scrutinyDetail;
	}

	protected OccupancyTypeHelper getMostRestrictiveFar(Set<OccupancyTypeHelper> distinctOccupancyTypes) {
		Set<String> codes = new HashSet<>();
		Map<String, OccupancyTypeHelper> codesMap = new HashMap<>();
		for (OccupancyTypeHelper typeHelper : distinctOccupancyTypes) {

			if (typeHelper.getType() != null)
				codesMap.put(typeHelper.getType().getCode(), typeHelper);
			if (typeHelper.getSubtype() != null)
				codesMap.put(typeHelper.getSubtype().getCode(), typeHelper);
		}
		codes = codesMap.keySet();
		if (codes.contains(T1c))
			return codesMap.get(T1c);
		if (codes.contains(T1b))
			return codesMap.get(T1b);
		if (codes.contains(T1a))
			return codesMap.get(T1a);
		if (codes.contains(T))
			return codesMap.get(T);
		if (codes.contains(P3c))
			return codesMap.get(P3c);
		if (codes.contains(P3b))
			return codesMap.get(P3b);
		if (codes.contains(P3a))
			return codesMap.get(P3a);
		if (codes.contains(P2d))
			return codesMap.get(P2d);
		if (codes.contains(P2c))
			return codesMap.get(P2c);
		if (codes.contains(P2b))
			return codesMap.get(P2b);
		if (codes.contains(P2a))
			return codesMap.get(P2a);
		if (codes.contains(P1g))
			return codesMap.get(P1g);
		if (codes.contains(P1f))
			return codesMap.get(P1f);
		if (codes.contains(P1e))
			return codesMap.get(P1e);
		if (codes.contains(P1d))
			return codesMap.get(P1d);
		if (codes.contains(P1c))
			return codesMap.get(P1c);
		if (codes.contains(P1b))
			return codesMap.get(P1b);
		if (codes.contains(P1a))
			return codesMap.get(P1a);
		if (codes.contains(P))
			return codesMap.get(P);
		if (codes.contains(G3c))
			return codesMap.get(G3c);
		if (codes.contains(G3b))
			return codesMap.get(G3b);
		if (codes.contains(G3a))
			return codesMap.get(G3a);
		if (codes.contains(G2a))
			return codesMap.get(G2a);
		if (codes.contains(G1a))
			return codesMap.get(G1a);
		if (codes.contains(G))
			return codesMap.get(G);
		if (codes.contains(I1b))
			return codesMap.get(I1b);
		if (codes.contains(I1a))
			return codesMap.get(I1a);
		if (codes.contains(I))
			return codesMap.get(I);
		if (codes.contains(C6a))
			return codesMap.get(C6a);
		if (codes.contains(C5a))
			return codesMap.get(C5a);
		if (codes.contains(C4a))
			return codesMap.get(C4a);
		if (codes.contains(C3a))
			return codesMap.get(C3a);
		if (codes.contains(C2a))
			return codesMap.get(C2a);
		if (codes.contains(C1c))
			return codesMap.get(C1c);
		if (codes.contains(C1b))
			return codesMap.get(C1b);
		if (codes.contains(C1a))
			return codesMap.get(C1a);
		if (codes.contains(C))
			return codesMap.get(C);
		if (codes.contains(R2a))
			return codesMap.get(R2a);
		if (codes.contains(R1c))
			return codesMap.get(R1c);
		if (codes.contains(R1b))
			return codesMap.get(R1b);
		if (codes.contains(R1a))
			return codesMap.get(R1a);
		if (codes.contains(R))
			return codesMap.get(R);
		else
			return null;

	}

	@Override
	public Map<String, Date> getAmendments() {
		return new LinkedHashMap<>();
	}
}
