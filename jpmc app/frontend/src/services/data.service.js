import axios from "axios";
import { API_URL } from "../common/constants";
import {
  apiGetAllSkillFamily,
  apiGetAllProfiles,
  apiEvalautionResults,
  apiProfileRejectionCategories,
  apiGetBillablePlan,
  apiGetBillableCategories,
  apiGetPracticeList,
  apiGetBillabilityReportData,
  apiGetFilteredBillablePlan,
  apiGetAssociateBillabilityHistory,
  getAllDrives,
  getAllServiceLines,
  apiGetLOBList,
  apiGetSettingsData,
  apiSaveSettingsData,
  apiGetAllReferrals,
  apiGetAllCategoryFamily,
  apiGetAssociateData,
  apiGetSearchAssociateData,
  apiGetAssignmentListing,
} from "../utils/AppUtils";

const getAllSkillFamily = () => {
  return apiGetAllSkillFamily();
};

const getAllProfiles = () => {
  return apiGetAllProfiles();
};

//----------------------------------
const getAllReferrals = () => {
  return apiGetAllReferrals();
};
//-----------------------------------
const getAssociateData = (associateId) => {
  return apiGetSearchAssociateData(associateId);
};

const getEvaluationResults = () => {
  return apiEvalautionResults();
};

const getRejectionCategories = () => {
  return apiProfileRejectionCategories();
};

const getBillableCategories = () => {
  return apiGetBillableCategories();
};

const getPracticeList = () => {
  return apiGetPracticeList();
};

const getLOBList = () => {
  return apiGetLOBList();
};

const getBillabilityReportData = (selPractice) => {
  return apiGetBillabilityReportData(selPractice);
};

const getBillablePlan = (selPractice, selLOB) => {
  return apiGetBillablePlan(selPractice, selLOB);
};

const getFilteredBillablePlan = (selPractice, categoryId, grade, location) => {
  return apiGetFilteredBillablePlan(selPractice, categoryId, grade, location);
};

const getAssociateBillabilityHistory = (associateId) => {
  return apiGetAssociateBillabilityHistory(associateId);
};

const getAllInterviewDrives = () => {
  return getAllDrives();
};

const getAllServiceLine = () => {
  return getAllServiceLines();
};

const getSettingsData = () => {
  return apiGetSettingsData();
};

const saveSettingsData = (changedData) => {
  return apiSaveSettingsData(changedData);
};

const getAllCategoryFamily = () => {
  return apiGetAllCategoryFamily();
};

const getAssignmentListing = (selPractice) => {
  return apiGetAssignmentListing(selPractice);
};

const DataService = {
  getAllSkillFamily,
  getAllProfiles,
  getEvaluationResults,
  getRejectionCategories,
  getBillablePlan,
  getFilteredBillablePlan,
  getBillableCategories,
  getPracticeList,
  getLOBList,
  getBillabilityReportData,
  getAssociateBillabilityHistory,
  getAllInterviewDrives,
  getAllServiceLine,
  getSettingsData,
  saveSettingsData,
  getAllReferrals,
  getAllCategoryFamily,
  getAssociateData,
  getAssignmentListing,
};

export default DataService;
