import React, { useState, useEffect } from "react";
import DataService from "../services/data.service";
import InterviewDriveService from "../services/InterViewDrive.service";
import { Link, useSearchParams } from "react-router-dom";
import {
  apiGetServiceLineList,
  apiGetAllUserLeavesByServiceLine,
} from "../utils/AppUtils";
import { apiGetAllLeavesCategory } from "../utils/AppUtils";
import HomeService from "../services/home.service";
import { useForm, Controller } from "react-hook-form";
import Select from "react-select";
import { Form, FormGroup, Label, Input, Button } from "reactstrap";
import "../css/home.css";
import "../css/Edldashboard.css";
import trainingService from "../services/training.service";
import NominateTraining from "../forms/training/NominateTraining";
import classes from "../forms/training/TrainingComponents.module.css";
import TrainingDevWatermark from "../forms/training/TrainingDevWatermark";
import TrainingProgress from "../forms/training/TrainingProgress";

const Edldashboard = ({ data, year }) => {
  const [trainingData, setTrainingData] = useState([]);
  const [serverError, setServerError] = useState("");
  const [isNominating, setIsNominating] = useState(false);
  const [drivesData, setDrivesData] = useState([]);
  const [hasDrives, setHasDrives] = useState(false);
  const [searchParams, setSearchParams] = useSearchParams();
  const showAvgProgress = searchParams.get("progress") == "avg";
  const [selectedTraining, setSelectedTraining] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [content, setContent] = useState([]);
  const [assignmentId, setassignmentId] = useState(1);
  const [profilesData, setProfilesData] = useState(null);
  const [leaveData, setLeaveData] = useState([]);
  const [statData, setStatData] = useState([]);
  const [reportViews, setReportViews] = useState([]);
  const [selected, setSelected] = useState(2);
  const [serviceLine, setServiceLine] = useState("ADM");
  const [skillViews, setSkillViews] = useState(null);
  const [selectServiceLine, setSelectServiceLine] = useState(null);
  const [hasRecords, setHasRecords] = useState(false);
  const { register, handleSubmit, errors, control } = useForm({
    // use mode to specify the event that triggers each input field
    mode: "onBlur",
  });

  const fetchLeaveData = () => {
    apiGetAllUserLeavesByServiceLine(new Date().getFullYear(), serviceLine)
      .then((data) => {
        console.log(data);
        setLeaveData(data);
      })
      .catch((err) => {
        setLeaveData([]);
        console.log(err);
      });
  };
  useEffect(() => {
    DataService.getAllInterviewDrives().then((data) => {
      console.log(data);
      setDrivesData(data);
      setHasDrives(data.length > 0);
    });
  }, []);

  useEffect(() => {
    getTrainingData();
  }, []);

  const getTrainingData = () => {
    setIsLoading(true);
    trainingService
      .getPersonalisedTrainings()
      .then(
        (response) => {
          setTrainingData(response.data);
          setHasRecords(response.data.length > 0);
        },
        (error) => {
          const _content =
            (error.response && error.response) ||
            error.message ||
            error.toString();

          setServerError(_content);
          //window.alert(_content);
        }
      )
      .finally(() => {
        setIsLoading(false);
        // setTrainingData(DUMMY_TRAININGS);
      });
  };

  const OnNominateHandler = (e) => {
    e.preventDefault();
    const id = e.target.dataset.id;
    const trainingId = trainingData[id].training.id;
    const trainingTitle = trainingData[id].training.title;

    setSelectedTraining({ id, trainingId, trainingTitle });
    setIsNominating(true);
  };

  useEffect(() => {
    const handleChange = () => {
      getReportData(1, assignmentId, serviceLine);
    };
    handleChange();
    fetchLeaveData();
  }, [assignmentId, serviceLine]);

  //for the serviceLine list
  useEffect(() => {
    apiGetServiceLineList().then((response) => {
      let temp = [];
      response.map((line) => {
        temp.push({ value: line, label: line });
      });
      setSelectServiceLine(temp);
    });
  }, []);

  //for the skill family to
  useEffect(() => {
    DataService.getAllProfiles().then(
      (response) => {
        console.log(response);
        setProfilesData(response);
      },
      (error) => {
        const _content =
          (error.response && error.response) ||
          error.message ||
          error.toString();
      }
    );
  }, []);

  useEffect(() => {
    DataService.getAllSkillFamily().then(
      (response) => {
        const skillView = [];
        response.forEach((skill, index) => {
          skillView.push({
            value: skill.id,
            label: skill.skillName,
          });
        });
        setSkillViews(skillView);
      },
      (error) => {
        const _content =
          (error.response && error.response) ||
          error.message ||
          error.toString();

        // setMessage(_content);
      }
    );
  }, [profilesData]);

  useEffect(() => {
    HomeService.getHomeContent().then(
      (response) => {
        setContent(response);
        const reportView = [];
        let idVal;
        let filenameVal;

        response.forEach((assignment, index) => {
          idVal = assignment.id;
          filenameVal = assignment.id + ". " + assignment.filename;
          reportView.push({
            value: idVal,
            label: filenameVal,
          });
        });
        setReportViews(reportView);
      },
      (error) => {
        console.log(error);
        const _content =
          (error.response && error.response) ||
          error.message ||
          error.toString();

        setContent(_content);
      }
    );
  }, []);

  const onSubmit = (data) => {};

  const handleError = (errors) => {};

  const getReportData = (reportID, paramId, serviceLine) => {
    HomeService.getAssignmentReportStat(reportID, paramId, serviceLine).then(
      (response) => {
        setStatData(response);
        setHasRecords(response.length > 0);
      },
      (error) => {
        const _content =
          (error.response && error.response) ||
          error.message ||
          error.toString();

        setContent(_content);
      }
    );
  };
  const handleChange = (event) => {
    setSelected(event.value);
    getReportData(1, event.value, serviceLine);
  };

  const getPercentage = (number, total) => {
    if (total > 0) return ((number * 100) / total).toFixed(1) + "%";
    else return "-";
  };

  const renderTableData = () => {
    let allRowTotalFTE = 0.0;
    let offshoreBillTotalFTE = 0.0;
    let offshoreNBLTotalFTE = 0.0;
    let onsiteBillTotalFTE = 0.0;
    let onsiteNBLTotalFTE = 0.0;
    statData.forEach((row, index) => {
      allRowTotalFTE = Math.round(
        allRowTotalFTE +
          row.offshoreBillFte +
          row.offshoreNBLFte +
          row.onsiteBillFte +
          row.onsiteNBLFte
      );

      offshoreBillTotalFTE = Math.round(
        offshoreBillTotalFTE + row.offshoreBillFte
      );
      offshoreNBLTotalFTE = Math.round(
        offshoreNBLTotalFTE + row.offshoreNBLFte
      );
      onsiteBillTotalFTE = Math.round(onsiteBillTotalFTE + row.onsiteBillFte);
      onsiteNBLTotalFTE = Math.round(onsiteNBLTotalFTE + row.onsiteNBLFte);
    });

    return statData.map((row, index) => {
      let totalFte;

      totalFte = Math.round(
        row.offshoreBillFte +
          row.offshoreNBLFte +
          row.onsiteBillFte +
          row.onsiteNBLFte
      );
    }); //ends return loop
  };
  var billable = 0;
  statData.map((row, index) => {
    let totalFte;
    totalFte = row.offshoreBillFte + row.onsiteBillFte;

    billable = billable + totalFte;
    return billable;
  });

  var total = 0;
    statData.map((row, index) => {
      let totalFte;
      totalFte =
        row.offshoreBillFte +
        row.offshoreNBLFte +
        row.onsiteBillFte +
        row.onsiteNBLFte;

      total = totalFte + total;
      return 0;
    });


  const getBillablePercentage = () => {
    var billable = 0;
    statData.map((row, index) => {
      let totalFte;
      totalFte = row.offshoreBillFte + row.onsiteBillFte;

      billable = billable + totalFte;
      return billable;
    });
    console.log(billable);
    console.log(total);
    var total = 0;
    statData.map((row, index) => {
      let totalFte;
      totalFte =
        row.offshoreBillFte +
        row.offshoreNBLFte +
        row.onsiteBillFte +
        row.onsiteNBLFte;

      total = totalFte + total;
      return 0;
    });

    return getPercentage(billable, total);
    
  };

  return (
    <div>
      <div>
        <div>
          <table>
            <tr className="assignmentdropdown">
              <td className="tddropdownlabel">Assignent Reports:</td>
              <td className="tdheaderelements">
                <Form onSubmit={handleSubmit(onSubmit, handleError)}>
                  <FormGroup>
                    <Select
                      onChange={(e) => {
                        const { name, value } = e;
                        setassignmentId(value);
                      }}
                      options={reportViews}
                    ></Select>
                  </FormGroup>
                </Form>
              </td>

              <td className="tddropdownlabel">
                <Label> &nbsp; &nbsp; &nbsp; Service Line:</Label>
              </td>
              <td className="tdheaderelements">
                {" "}
                <Select
                  defaultValue={{ label: serviceLine, value: serviceLine }}
                  name="serviceLine"
                  // className="yearDropDown"
                  options={selectServiceLine}
                  onChange={(e) => {
                    const { name, value } = e;
                    setServiceLine(value);
                  }}
                ></Select>{" "}
              </td>
            </tr>
          </table>
          <div
            style={{
              display: "grid",
              gridTemplateColumns: "1fr 1fr 1fr 1fr 1fr",
              margin: "10px",
            }}
          >
            <table className="dashboardmaintable">
              <tbody>
                <tr>
                  <td>
                    <div className="dashboardbox">
                      <div className="dashboardboxheading">Billable</div>
                      <div className="dashboardboxvalue">
                        {Math.round(billable)}/{Math.round(total)}
                      </div>
                      <div className="dashboardboxpercentagebox">
                        <div className="dashboardboxpercentagetitle">
                          Billability
                        </div>
                        <div className="dashboardboxpercentage">
                        {statData != [] && getBillablePercentage()}
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
                      width: statData != [] && getBillablePercentage(),
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

            
          </div>
         Contracts and Pyramid table 
          <div
            style={{
              display: "grid",
              gridTemplateColumns: "1fr 1fr",
              marginLeft: "20px",
            }}
          >
            {/* Contracts Table */}
            <div>
              <div
                style={{
                  width: "95%",
                  border: "2px solid #7ebdf8",
                  borderRadius: "5px",
                  margin: "4px",
                }}
              >
                <h4
                  style={{
                    color: "SlateBlue",
                    fontSize: "20px",
                    marginLeft: "20px",
                  }}
                >
                  Contracts - In Progress
                </h4>
                <table>
                  <thead>
                    <tr>
                      <th style={{ backgroundColor: "#7ebdf8" }}>Title</th>
                      <th style={{ backgroundColor: "#7ebdf8" }}>LOB</th>
                      <th style={{ backgroundColor: "#7ebdf8" }}>TCV</th>
                      <th style={{ backgroundColor: "#7ebdf8" }}>Status</th>
                    </tr>
                  </thead>

                  <tbody>
                    {skillViews &&
                      skillViews.map((skill) => {
                        return (
                          <tr>
                            <td>{skill.label}</td>
                            <td>10</td>
                            <td>8</td>
                            <td>10</td>
                          </tr>
                        );
                      })}
                  </tbody>
                </table>
              </div>
            </div>

            {/* Pyramind Table */}
            <div>
              <div
                style={{
                  width: "95%",
                  border: "2px solid #7ebdf8",
                  borderRadius: "5px",
                  margin: "4px",
                }}
              >
                <h4
                  style={{
                    color: "SlateBlue",
                    fontSize: "20px",
                    marginLeft: "20px",
                  }}
                >
                  Pyramid
                </h4>
                <table>
                  <thead>
                    <tr>
                      <th style={{ backgroundColor: "#7ebdf8" }}> Grade</th>
                      <th style={{ backgroundColor: "#7ebdf8" }}> BTM</th>
                      <th style={{ backgroundColor: "#7ebdf8" }}> BFD</th>
                      <th style={{ backgroundColor: "#7ebdf8" }}> Total</th>
                    </tr>
                  </thead>

                  <tbody>
                    {statData.map((row, index) => (
                      <tr>
                        <td>{row.grade}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
          </div>

          {/* Issues Table */}
          <div
            style={{
              width: "96%",
              border: "2px solid #7ebdf8",
              borderRadius: "5px",
              margin: "10px",
              marginTop: "15px",
              marginLeft: "25px",
            }}
          >
            <h4
              style={{
                color: "SlateBlue",
                fontSize: "20px",
                marginLeft: "28px",
              }}
            >
              Issues/Risks
            </h4>
            <table>
              <thead>
                <tr>
                  <th style={{ backgroundColor: "#7ebdf8" }}>Issues/Risk</th>
                  <th style={{ backgroundColor: "#7ebdf8" }}>Owner</th>
                  <th style={{ backgroundColor: "#7ebdf8" }}>ETA</th>
                  <th style={{ backgroundColor: "#7ebdf8" }}>Status</th>
                  <th style={{ backgroundColor: "#7ebdf8" }}>Impact</th>
                  <th style={{ backgroundColor: "#7ebdf8" }}>Create Date</th>
                  <th style={{ backgroundColor: "#7ebdf8" }}>LOB</th>
                </tr>
              </thead>
              <tbody>
                {skillViews &&
                  skillViews.map((skill) => {
                    return (
                      <tr>
                        <td>{skill.label}</td>
                      </tr>
                    );
                  })}
              </tbody>
            </table>
          </div>

          {/* Appreciations Table */}
          <div
            style={{
              width: "96%",
              border: "2px solid #7ebdf8",
              borderRadius: "5px",
              margin: "10px",
              marginTop: "15px",
              marginLeft: "25px",
            }}
          >
            <h4
              style={{
                color: "SlateBlue",
                fontSize: "20px",
                marginLeft: "28px",
              }}
            >
              Appreciations
            </h4>
            <table>
              <thead>
                <tr>
                  <th style={{ backgroundColor: "#7ebdf8" }}>LOB</th>
                  <th style={{ backgroundColor: "#7ebdf8" }}>Projects</th>
                  <th style={{ backgroundColor: "#7ebdf8" }}>
                    Indiviual Appreciation
                  </th>
                  <th style={{ backgroundColor: "#7ebdf8" }}>Team Appreciations</th>
                  <th style={{ backgroundColor: "#7ebdf8" }}>LOB</th>
                </tr>
              </thead>
              <tbody>
                {skillViews &&
                  skillViews.map((skill) => {
                    return (
                      <tr>
                        <td>{skill.label}</td>
                      </tr>
                    );
                  })}
              </tbody>
            </table>
          </div>
        </div>

        <div
          style={{
            display: "grid",
            gridTemplateColumns: "1fr 1fr",
            marginLeft: "20px",
          }}
        >
          {/* Panels Bar Graph */}
          <div>
            <div
              style={{
                width: "95%",
                height:"380px",
                border: "2px solid #7ebdf8",
                borderRadius: "5px",
                margin: "4px",
              }}
            >
              <h4
                style={{
                  color: "SlateBlue",
                  fontSize: "20px",
                  marginLeft: "20px",
                }}
              >
                Panels
              </h4>
             
              <div class="chart">
               <ul class="numbers">
                 <li><span>60</span></li>
                 <li><span>50</span></li>
                 <li><span>40</span></li>
                 <li><span>30</span></li>
                 <li><span>20</span></li>
                 <li><span>10</span></li>
                 <li><span>0</span></li>
               </ul>
               <ul class="bars">
                 <li><div class="bar" style={{height:`${drivesData &&
                    drivesData
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getFullYear() ==
                          new Date().getFullYear()
                        );
                      })
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getMonth() == 0
                        );
                      }).length}px`}}><span>Jan</span></div></li>
                
                 <li><div class="bar" style={{height:`${drivesData &&
                    drivesData
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getFullYear() ==
                          new Date().getFullYear()
                        );
                      })
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getMonth() == 1
                        );
                      }).length}px`}}><span>Feb</span></div></li>
                 <li><div class="bar" style={{height:`${drivesData &&
                    drivesData
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getFullYear() ==
                          new Date().getFullYear()
                        );
                      })
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getMonth() == 2
                        );
                      }).length}px`}}><span>Mar</span></div></li>
                 <li><div class="bar" style={{height:`${drivesData &&
                    drivesData
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getFullYear() ==
                          new Date().getFullYear()
                        );
                      })
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getMonth() == 3
                        );
                      }).length}px`}}><span>Apr</span></div></li>
                 <li><div class="bar" style={{height:`${drivesData &&
                    drivesData
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getFullYear() ==
                          new Date().getFullYear()
                        );
                      })
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getMonth() == 4
                        );
                      }).length}px`}}><span>May</span></div></li>
                 <li><div class="bar" style={{height:`${drivesData &&
                    drivesData
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getFullYear() ==
                          new Date().getFullYear()
                        );
                      })
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getMonth() == 5
                        );
                      }).length}px`}}><span>Jun</span></div></li>
                 <li><div class="bar" style={{height:`${drivesData &&
                    drivesData
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getFullYear() ==
                          new Date().getFullYear()
                        );
                      })
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getMonth() == 6
                        );
                      }).length}px`}}><span>Jul</span></div></li>
                 <li><div class="bar" style={{height:`${drivesData &&
                    drivesData
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getFullYear() ==
                          new Date().getFullYear()
                        );
                      })
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getMonth() == 7
                        );
                      }).length}px`}}><span>Aug</span></div></li>
                 <li><div class="bar" style={{height:`${drivesData &&
                    drivesData
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getFullYear() ==
                          new Date().getFullYear()
                        );
                      })
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getMonth() == 8
                        );
                      }).length}px`}}><span>Sep</span></div></li>
                 <li><div class="bar" style={{height:`${drivesData &&
                    drivesData
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getFullYear() ==
                          new Date().getFullYear()
                        );
                      })
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getMonth() == 9
                        );
                      }).length}px`}}><span>Oct</span></div></li>
                 <li><div class="bar" style={{height:`${drivesData &&
                    drivesData
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getFullYear() ==
                          new Date().getFullYear()
                        );
                      })
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getMonth() == 10
                        );
                      }).length}px`}}><span>Nov</span></div></li>
                 <li><div class="bar" style={{height:`${drivesData &&
                    drivesData
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getFullYear() ==
                          new Date().getFullYear()
                        );
                      })
                      .filter((drive) => {
                        return (
                          new Date(drive.interviewDriveDate).getMonth() == 11
                        );
                      }).length}px`}}><span>Dec</span></div></li>
               </ul>
             </div>
            </div>
          </div>

          {/* Trainings Table */}
          <div>
            <div
              style={{
                width: "95%",
                border: "1px solid #7ebdf8",
                borderRadius: "5px",
                margin: "4px",
              }}
            >
              <h4
                style={{
                  color: "SlateBlue",
                  fontSize: "20px",
                  marginLeft: "20px",
                }}
              >
                Trainings
              </h4>
              <table>
                <thead>
                  <tr>
                    <th
                      style={{ backgroundColor: "white", alignItems: "center" }}
                    >
                      Skill Family
                    </th>
                    <th style={{ backgroundColor: "white" }}>Completed</th>
                    <th style={{ backgroundColor: "white" }}>In Progress</th>
                    <th style={{ backgroundColor: "white" }}>To Be Started</th>
                  </tr>
                </thead>
                <tbody>
                  {skillViews &&
                    skillViews.map((skill) => {
                      return (
                        <tr>
                          <td style={{ borderTop: "1px solid #6ab5fb" }}>
                            {skill.label}
                          </td>

                          <td style={{ borderTop: "1px solid #6ab5fb" }}>
                            {trainingData &&
                              trainingData
                                .filter(
                                  (training) =>
                                    skill.label ==
                                    training.training.skill.skillName
                                )
                                .filter((training) => {
                                  return (
                                    new Date(
                                      training.training.endDate
                                    ).getTime() < new Date().getTime()
                                  );
                                }).length}
                          </td>

                          <td style={{ borderTop: "1px solid #6ab5fb" }}>
                            {trainingData &&
                              trainingData
                                .filter(
                                  (training) =>
                                    skill.label ==
                                    training.training.skill.skillName
                                )
                                .filter((training) => {
                                  return (
                                    new Date(
                                      training.training.startDate
                                    ).getTime() < new Date().getTime() &&
                                    new Date(
                                      training.training.endDate
                                    ).getTime() > new Date().getTime()
                                  );
                                }).length}
                          </td>

                          <td style={{ borderTop: "1px solid #6ab5fb" }}>
                            {trainingData &&
                              trainingData
                                .filter(
                                  (training) =>
                                    skill.label ==
                                    training.training.skill.skillName
                                )
                                .filter((training) => {
                                  return (
                                    new Date(
                                      training.training.startDate
                                    ).getTime() > new Date().getTime()
                                  );
                                }).length}
                          </td>
                        </tr>
                      );
                    })}
                </tbody>

                <tbody></tbody>
              </table>
            </div>
          </div>
        </div>

        {/* Referrals and Profile Evaluation  */}
        <div
          style={{
            display: "grid",
            gridTemplateColumns: "1fr 1fr",
            marginLeft: "20px",
          }}
        >
          {/* Contracts Table */}
          <div>
            <div
              style={{
                width: "95%",
                border: "2px solid #7ebdf8",
                borderRadius: "5px",
                margin: "4px",
              }}
            >
              <h4
                style={{
                  color: "SlateBlue",
                  fontSize: "20px",
                  marginLeft: "20px",
                }}
              >
                Referred
              </h4>
              <table>
                <thead>
                  <tr>
                    <th style={{ backgroundColor: "#7ebdf8" }}>Skill Family</th>
                    <th style={{ backgroundColor: "#7ebdf8" }}>Referred</th>
                    <th style={{ backgroundColor: "#7ebdf8" }}>Joined</th>
                    <th style={{ backgroundColor: "#7ebdf8" }}>Offer</th>
                    <th style={{ backgroundColor: "#7ebdf8" }}>Declined</th>
                    <th style={{ backgroundColor: "#7ebdf8" }}>Offer</th>
                    <th style={{ backgroundColor: "#7ebdf8" }}>Interview</th>
                    <th style={{ backgroundColor: "#7ebdf8" }}>Hold</th>
                  </tr>
                </thead>

                <tbody>
                  {skillViews &&
                    skillViews.map((skill) => {
                      return (
                        <tr>
                          <td>{skill.label}</td>
                        </tr>
                      );
                    })}
                </tbody>
              </table>
            </div>
          </div>

          {/* Pyramind Table */}
          <div>
            <div
              style={{
                width: "95%",
                border: "2px solid #7ebdf8",
                borderRadius: "5px",
                margin: "4px",
              }}
            >
              <h4
                style={{
                  color: "SlateBlue",
                  fontSize: "20px",
                  marginLeft: "20px",
                }}
              >
                Profile Evaluation
              </h4>
              <table>
                <thead>
                  <tr>
                    <th style={{ backgroundColor: "white" }}> Status</th>
                    <th style={{ backgroundColor: "white" }}> Internal</th>
                    <th style={{ backgroundColor: "white" }}> External</th>
                  </tr>
                </thead>

                <tbody>
                  {skillViews &&
                    skillViews.map((skill) => {
                      return <tr></tr>;
                    })}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Edldashboard;
