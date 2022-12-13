import React, { useState, useEffect } from "react";
import DataService from "../services/data.service";
import GenCTrackerService from "../services/genctracker.service";
import Button from "react-validation/build/button";
import "../css/main.css";
import { UI_URL } from "../common/constants";
import SaveIcon from "../images/saveicon.svg";
import { Link } from "react-router-dom";
import { useLocation, useNavigate } from "react-router";
import InterviewDriveService from "../services/InterViewDrive.service";
import { Form, FormGroup, Label, Input } from "reactstrap";

const GenCTrackerDashboard = () => {
  const [listData, setListData] = useState([]);
  const [hasRecords, setHasRecords] = useState(false);
  const [message, setMessage] = useState(false);
  const [billableGenCs, setBillableGenCs] = useState([]);
  const [totalGenCs, setTotalGenCs] = useState([]);
  const [totalStories, setTotalStories] = useState(null);
  const [acceptedStories, setAcceptedStories] = useState(null);
  const [completedSprints, setCompletedSprints] = useState(null);
  const [totalSprints, setTotalSprints] = useState(null);
  const [totalEpics, setTotalEpics] = useState(0);
  const [completedEpics, setCompletedEpics] = useState(0);
  const [currentSprintGenCs, setCurrentSprintGenCs] = useState(0);

  const columns = [
    { label: "S. No.", colSize: "5" },
    { accessor: "parentAccount", label: "Parent Account", colSize: "10" },
    { accessor: "totalGenCs", label: "Total GenCs", colSize: "40" },
    { accessor: "billableGenCs", label: "Billable GenCs", colSize: "20" },
    { accessor: "totalEpics", label: "Total Epics", colSize: "5" },
    { accessor: "completedEpics", label: "Completed Epics", colSize: "5" },
    { accessor: "totalSprints", label: "Total Sprints", colSize: "5" },
    { accessor: "completedSprints", label: "Completed Sprints", colSize: "5" },
    { accessor: "totalStories", label: "Total Stories", colSize: "5" },
    { accessor: "completedStories", label: "Completed Stories", colSize: "5" },
    {
      accessor: "totalGenCsHaveStories",
      label: "GenCs Having Stories",
      colSize: "5",
    },
  ];

  useEffect(() => {
    GenCTrackerService.getGenCDashboardMetrics(254395).then(
      (response) => {
        console.log(response);
        setBillableGenCs(response.billableGenCs);
        setTotalGenCs(response.totalGenCs);
        setTotalStories(response.totalStories);
        setAcceptedStories(response.acceptedStories);
        setTotalSprints(response.totalSprints);
        setCompletedSprints(response.completedSprints);
        setCurrentSprintGenCs(response.currentSprintGenCs);
        setListData(response.genCGroupDetails);
      },
      (error) => {
        if ((error.response && error.response) || error.message)
          setMessage(error.toString());
      }
    );
  }, []);

  const printContent = (column, row) => {
    return row[column.accessor];
  };

  const getRandomKey = () => {
    return 1 + Math.random() * (99999999999 - 1);
  };

  const toPercentage = (number, total) => {
    if (
      !total ||
      parseFloat(total) == 0.0 ||
      !number ||
      parseFloat(number) == 0.0
    )
      return "";

    let value = parseFloat((number * 100) / total);
    if (value == 0.0) return "";

    return value.toFixed(0) + "%";
  };

  return (
    <div className="mainDiv">
      <table className="dashboardmaintable">
        <tbody>
          <tr>
            <td>
              <div className="dashboardbox">
                <div className="dashboardboxheading">Billable GenCs</div>
                <div className="dashboardboxvalue">
                  {billableGenCs}/{totalGenCs}
                </div>
                <div className="dashboardboxpercentagebox">
                  <div className="dashboardboxpercentagetitle">Billability</div>
                  <div className="dashboardboxpercentage">
                    {toPercentage(billableGenCs, totalGenCs)}
                  </div>
                </div>
                <div
                  style={{
                    height: 5,
                    border: "1px solid #ffffff",
                    color: "red",
                  }}
                >
                  <div
                    style={{
                      float: "left",
                      width: toPercentage(billableGenCs, totalGenCs),
                      height: "100%",
                      backgroundColor: "#0018f1",
                    }}
                  ></div>
                  <div
                    style={{
                      width: "100%",
                      height: "100%",
                      backgroundColor: "#e4e5f7",
                    }}
                  ></div>
                </div>
              </div>
            </td>
            <td>
              <div className="dashboardbox">
                <div className="dashboardboxheading">Epics</div>
                <div className="dashboardboxvalue">{`${completedEpics}/${totalEpics}`}</div>
                <div className="dashboardboxpercentagebox">
                  <div className="dashboardboxpercentagetitle">Completed</div>
                  <div className="dashboardboxpercentage">
                    {toPercentage(completedEpics, totalEpics)}
                  </div>
                </div>
                <div
                  style={{
                    height: 5,
                    border: "1px solid #ffffff",
                    color: "red",
                  }}
                >
                  <div
                    style={{
                      float: "left",
                      width: toPercentage(completedEpics, totalEpics),
                      height: "100%",
                      backgroundColor: "#0018f1",
                    }}
                  ></div>
                  <div
                    style={{
                      width: "100%",
                      height: "100%",
                      backgroundColor: "#e4e5f7",
                    }}
                  ></div>
                </div>
              </div>
            </td>
            <td>
              <div className="dashboardbox">
                <div className="dashboardboxheading">Sprints</div>
                <div className="dashboardboxvalue">
                  {completedSprints}/{totalSprints}
                </div>
                <div className="dashboardboxpercentagebox">
                  <div className="dashboardboxpercentagetitle">Delievered</div>
                  <div className="dashboardboxpercentage">
                    {toPercentage(completedSprints, totalSprints)}
                  </div>
                </div>
                <div
                  style={{
                    height: 5,
                    border: "1px solid #ffffff",
                    color: "red",
                  }}
                >
                  <div
                    style={{
                      float: "left",
                      width: toPercentage(completedSprints, totalSprints),
                      height: "100%",
                      backgroundColor: "#0018f1",
                    }}
                  ></div>
                  <div
                    style={{
                      width: "100%",
                      height: "100%",
                      backgroundColor: "#e4e5f7",
                    }}
                  ></div>
                </div>
              </div>
            </td>
            <td>
              <div className="dashboardbox">
                <div className="dashboardboxheading">Stories</div>
                <div className="dashboardboxvalue">
                  {acceptedStories}/{totalStories}
                </div>
                <div className="dashboardboxpercentagebox">
                  <div className="dashboardboxpercentagetitle">Accepted</div>
                  <div className="dashboardboxpercentage">
                    {toPercentage(acceptedStories, totalStories)}
                  </div>
                </div>
                <div
                  style={{
                    height: 5,
                    border: "1px solid #ffffff",
                    color: "red",
                  }}
                >
                  <div
                    style={{
                      float: "left",
                      width: toPercentage(acceptedStories, totalStories),
                      height: "100%",
                      backgroundColor: "#0018f1",
                    }}
                  ></div>
                  <div
                    style={{
                      width: "100%",
                      height: "100%",
                      backgroundColor: "#e4e5f7",
                    }}
                  ></div>
                </div>
              </div>
            </td>
            <td>
              <div className="dashboardbox">
                <div className="dashboardboxheading">GenC Util</div>
                <div className="dashboardboxvalue">{`${currentSprintGenCs}/${totalGenCs}`}</div>
                <div className="dashboardboxpercentagebox">
                  <div className="dashboardboxpercentagetitle">
                    Assigned to GenCs
                  </div>
                  <div className="dashboardboxpercentage">
                    {toPercentage(currentSprintGenCs, totalGenCs)}
                  </div>
                </div>
                <div
                  style={{
                    height: 5,
                    border: "1px solid #ffffff",
                    color: "red",
                  }}
                >
                  <div
                    style={{
                      float: "left",
                      width: toPercentage(currentSprintGenCs, totalGenCs),
                      height: "100%",
                      backgroundColor: "#0018f1",
                    }}
                  ></div>
                  <div
                    style={{
                      width: "100%",
                      height: "100%",
                      backgroundColor: "#e4e5f7",
                    }}
                  ></div>
                </div>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <table className="mainTable">
        <thead>
          <tr>
            {columns.map((column) => {
              return (
                <th key={getRandomKey()}>
                  <span>{column.label}</span>
                </th>
              );
            })}
          </tr>
        </thead>
        <tbody>
          {listData.map((row, rowIndex) => {
            return (
              <tr key={getRandomKey()} className="rowstyle">
                {columns.map((column, colIndex) => {
                  return colIndex == 0 ? (
                    rowIndex + 1
                  ) : (
                    <td key={getRandomKey()}>{printContent(column, row)}</td>
                  );
                })}
              </tr>
            );
          })}
        </tbody>
      </table>
    </div>
  );
};

export default GenCTrackerDashboard;
