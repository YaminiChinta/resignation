import React, { useState, useEffect } from "react";
import { apiGetAllSprints } from "../utils/AppUtils";
import { useNavigate } from "react-router-dom";

function AllSprints() {
  const [sprints, setSprints] = useState([]);
  const navigate = useNavigate();
  useEffect(() => {
    apiGetAllSprints()
      .then((response) => {
        setSprints(response);
        console.log(response);
      })
      .catch((err) => console.log(err));
  }, []);

  return (
    <div>
      {
        <div>
          <table className="gdvheaderTable">
            <tr className="gdvheader">
              <th className="groupname">Sprint Name</th>
              <th className="groupname">Start Date</th>
              <th className="groupname">End Date</th>
              <th className="groupname">Scrum Master</th>
            </tr>
            {sprints.map((sprint, index) => {
              return (
                <tr key={sprint.id} className="profilerow">
                  <td style={{ cursor: "pointer" }} className="tdcentercontent">
                    <a
                      href="#"
                      onClick={(e) => {
                        e.preventDefault();
                        navigate(
                          `/ui/gencTracker/sprint/${sprint.id}/sprintDetail`
                        );
                      }}
                    >
                      {sprint.name}
                    </a>
                  </td>
                  <td
                    style={{ paddingLeft: "10px", paddingRight: "10px" }}
                    className="tdcentercontent"
                  >
                    {new Date(sprint.startDate).toLocaleString()}
                  </td>
                  <td
                    style={{ paddingLeft: "10px", paddingRight: "10px" }}
                    className="tdcentercontent"
                  >
                    {new Date(sprint.endDate).toLocaleString()}
                  </td>
                  <td
                    style={{ paddingLeft: "10px", paddingRight: "10px" }}
                    className="tdcentercontent"
                  >
                    {sprint.scrumMasterName}
                  </td>
                </tr>
              );
            })}
          </table>
        </div>
      }
    </div>
  );
}

export default AllSprints;
