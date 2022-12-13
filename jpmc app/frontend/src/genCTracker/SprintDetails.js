import React, { useState, useRef, useEffect } from "react";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import Button from "react-validation/build/button";
import { required } from "../common/validators";
import {
  apiGetServiceLineList,
  apiGetAssignUsersFromServiceLine,
  apiCreateSprint,
  apiGetAllStories,
  apiGetSprintById,
} from "../utils/AppUtils";
import Select from "react-select";
import { useNavigate, useParams } from "react-router-dom";
import makeAnimated from "react-select/animated";

function SprintDetails() {
  //   const [sprint, setSprint] = useState({
  //     name: "",
  //     startDate: "",
  //     endDate: "",
  //     scrumMasterId: "",
  //     storyIds: [],
  //   });
  const [sprint, setSprint] = useState(null);
  const { id } = useParams();
  const animatedComponents = makeAnimated();
  const form = useRef();
  const selectedStories = useRef();
  const navigate = useNavigate();
  const DateFormat = "yyyy-MM-dd";
  //   const [serviceLineList, setServiceLineList] = useState([]);
  //   const [serviceLine, setServiceLine] = useState(null);
  //   const [associateIds, setAssociateIds] = useState([]);
  const [stories, setStories] = useState([]);

  useEffect(() => {
    apiGetAllStories()
      .then((response) => {
        setStories(response);
      })
      .catch((err) => console.log(err));
    apiGetSprintById(id)
      .then((response) => setSprint(response))
      .catch((err) => console.log(err));
  }, []);

  //   const handleChange = (e) => {
  //     e.preventDefault();
  //     const { name, value } = e.target;
  //     setSprint({ ...sprint, [name]: value });
  //   };

  //   useEffect(() => {
  //     apiGetServiceLineList().then((response) => {
  //       let temp = [];
  //       response.map((line) => {
  //         temp.push({ value: line, label: line });
  //       });
  //       setServiceLineList(temp);
  //     });
  //   }, []);

  //   useEffect(() => {
  //     apiGetAssignUsersFromServiceLine(serviceLine).then((response) => {
  //       //   console.log(response);
  //       setAssociateIds(response);
  //     });
  //   }, [serviceLine]);

  //   const handleSubmit = (e) => {
  //     e.preventDefault();
  //     console.log(selectedStories.current.props.value);
  //     form.current.validateAll();
  //     var data = { ...sprint };
  //     data.storyIds = selectedStories.current.props.value.map(
  //       (story) => story.value
  //     );
  //     data.startDate = new Date(data.startDate).toISOString();
  //     data.endDate = new Date(data.endDate).toISOString();
  //     console.log(data);
  //     apiCreateSprint(data)
  //       .then((response) => {
  //         alert(response.message);
  //         navigate("/ui/gencTracker/sprint/allSprints");
  //       })
  //       .catch((err) => console.log(err));
  //   };

  return (
    <div>
      <div className="card card-container-form">
        <label className="formheading">Sprint Details</label>
        {sprint && (
          <Form /*onSubmit={handleSubmit}*/ ref={form}>
            {
              <div>
                <table className="tableform">
                  <tbody>
                    <tr>
                      <td className="tableformlabels">
                        <label htmlFor="associateId">Name</label>
                      </td>
                      <td>
                        <Input
                          type="text"
                          className="form-control"
                          name="name"
                          value={sprint.name}
                          // onChange={handleChange}
                          validations={[required]}
                        />
                      </td>
                    </tr>
                    <tr>
                      <td className="tableformlabels">
                        <label htmlFor="fullName">Start Date</label>
                      </td>
                      <td>
                        <Input
                          type="text"
                          className="form-control"
                          name="startDate"
                          value={new Date(
                            sprint.startDate
                          ).toLocaleDateString()}
                          // onChange={handleChange}
                          validations={[required]}
                          rows="9"
                        />
                      </td>
                    </tr>
                    <tr>
                      <td className="tableformlabels">
                        <label htmlFor="fullName">End Date</label>
                      </td>
                      <td>
                        <Input
                          type="text"
                          className="form-control"
                          name="endDate"
                          value={new Date(sprint.endDate).toLocaleDateString()}
                          // onChange={handleChange}
                          validations={[required]}
                          rows="9"
                        />
                      </td>
                    </tr>

                    {/* <tr>
                    <td className="tableformlabels">
                      <label htmlFor="skillId">Service Line</label>
                    </td>
                    <td>
                      <Select
                        onChange={(e) => {
                          setServiceLine(e.value);
                        }}
                        options={serviceLineList}
                        placeholder="Select Service Line"
                      ></Select>
                    </td>
                  </tr> */}
                    <tr>
                      <td className="tableformlabels">
                        <label htmlFor="phone">Scrum Master</label>
                      </td>

                      <td>
                        <Input
                          type="text"
                          // onChange={(e) =>
                          //   setSprint({ ...sprint, scrumMasterId: e.value })
                          // }
                          // isDisabled={serviceLine == null}
                          // options={associateIds}
                          className="form-control"
                          value={sprint.scrumMasterName}
                          // defaultValue={{label:sprint.scrumMasterName,value:sprint.scrumMasterId}}
                          // placeholder="Select Scrum Master"
                          validations={[required]}
                        ></Input>
                      </td>
                    </tr>
                    {/* <tr>
                    <td className="tableformlabels"></td>
                    <td>
                      <Button
                        className="btn btn-primary btn-block formsubmitbutton"
                        type="submit"
                      >
                        Create
                      </Button>
                    </td>
                  </tr> */}
                  </tbody>
                </table>
              </div>
            }
          </Form>
        )}
      </div>
      <table>
        <tr>
          <th>Subject</th>
          <th>Created By</th>
          <th>Responsible</th>
          <th>Priority</th>
          <th>Epic</th>
          <th>Status</th>
        </tr>
        {sprint &&
          sprint.storyIds.map((id) => {
            return stories
              .filter((story) => story.id == id)
              .map((index) => {
                return (
                  <tr className="profilerow">
                    <td className="tdcentercontent">{index.subject}</td>
                    <td className="tdcentercontent">{index.creator}</td>
                    <td className="tdcentercontent">{index.responsible}</td>
                    <td className="tdcentercontent"> {index.priority}</td>
                    <td className="tdcentercontent">{index.epic}</td>
                    <td className="tdcentercontent">{index.currentStatus}</td>
                  </tr>
                );
              });
          })}
      </table>
    </div>
  );
}

export default SprintDetails;
