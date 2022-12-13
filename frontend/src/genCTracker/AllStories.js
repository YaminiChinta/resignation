import React, { useState, useEffect } from "react";
import { apiGetAllStories } from "../utils/AppUtils";
import { useNavigate, useSearchParams, Link } from "react-router-dom";
import classes from "./GenCTrackerComponents.module.css";

function AllStories() {
  const [stories, setStories] = useState([]);
  const navigate = useNavigate();
  const [searchParams, setSearchParams] = useSearchParams();
  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    setIsLoading(true);
    apiGetAllStories(searchParams.toString())
      .then((response) => {
        setStories(response);
        console.log(response);
      })
      .catch((err) => console.log(err))
      .finally(() => {
        setIsLoading(false);
      });
  }, []);

  return (
    <div>
      {!isLoading && (
        <div className={classes.container}>
          <h5 className={classes.formheading}>Stories</h5>
          <table className={classes.table}>
            <tr className={classes.tablehead}>
              <th>Sr no.</th>
              <th>Story Subject</th>
              <th>Responsible</th>
              <th>Epic</th>
              <th>Priority</th>
            </tr>
            {stories.map((story, index) => {
              return (
                <tr key={story.id} className={`${classes.tablerow}`}>
                  <td>{index + 1}</td>
                  <td style={{ cursor: "pointer" }} className="tdcentercontent">
                    <Link
                      className={classes.link}
                      to={`/ui/gencTracker/stories/${story.id}/storyDetails`}
                    >
                      {story.subject}
                      <span className="material-icons">link</span>
                    </Link>
                  </td>
                  <td
                    style={{ paddingLeft: "10px", paddingRight: "10px" }}
                    className="tdcentercontent"
                  >
                    {story.owner.associateName}
                  </td>
                  <td
                    style={{ paddingLeft: "10px", paddingRight: "10px" }}
                    className="tdcentercontent"
                  >
                    {story.epic.name}
                  </td>
                  <td
                    style={{ paddingLeft: "10px", paddingRight: "10px" }}
                    className="tdcentercontent"
                  >
                    {story.storyPriority.groupValue}
                  </td>
                </tr>
              );
            })}
          </table>
        </div>
      )}
      {isLoading && <div className="loader-container"></div>}
    </div>
  );
}

export default AllStories;
