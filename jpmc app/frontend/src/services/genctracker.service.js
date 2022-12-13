import { apiGetGenCDashboardMetrics } from "../utils/AppUtils";

const getGenCDashboardMetrics = (associateId) => {
  console.log("associateId=" + associateId);
  return apiGetGenCDashboardMetrics(associateId);
};



const GenCTrackerService = {
  getGenCDashboardMetrics,
};

export default GenCTrackerService;
