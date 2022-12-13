import React, { useState, useEffect } from "react";
import { apiGetAllStories } from "../utils/AppUtils";
import { useNavigate } from "react-router-dom";
function AllStories() {
  const [stories, setStories] = useState([]);
  const navigate = useNavigate();
  useEffect(() => {
    apiGetAllStories()
      .then((response) => {
        setStories(response);
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
              <th className="groupname">Story Subject</th>
              <th className="groupname">Created By</th>
              <th className="groupname">Responsible</th>
              <th className="groupname">Priority</th>
            </tr>
            {stories.map((story, index) => {
              return (
                <tr key={story.id} className="profilerow">
                  <td style={{ cursor: "pointer" }} className="tdcentercontent">
                    <a
                      href="#"
                      onClick={(e) => {
                        e.preventDefault();
                        navigate(
                          `/ui/gencTracker/stories/${story.id}/storyDetails`,
                          {
                            state: { id: story.id },
                          }
                        );
                      }}
                    >
                      {story.subject}
                    </a>
                  </td>
                  <td
                    style={{ paddingLeft: "10px", paddingRight: "10px" }}
                    className="tdcentercontent"
                  >
                    {story.creator}
                  </td>
                  <td
                    style={{ paddingLeft: "10px", paddingRight: "10px" }}
                    className="tdcentercontent"
                  >
                    {story.responsible}
                  </td>
                  <td
                    style={{ paddingLeft: "10px", paddingRight: "10px" }}
                    className="tdcentercontent"
                  >
                    {story.priority}
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

export default AllStories;
