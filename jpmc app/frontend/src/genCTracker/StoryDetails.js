import React, { useState, useRef, useEffect } from "react";
import {
  apiGetStoryById,
  apiGetAllEpics,
  apiUpdateStory,
  apiGetAllPriorities,
  apiGetAllStoryStatus,
  apiGetServiceLineList,
  apiGetAssignUsersFromServiceLine,
  apiDeleteStory,
} from "../utils/AppUtils";
import Select from "react-select";
import "../css/newstory.css";
import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import Button from "react-validation/build/button";
import { required } from "../common/validators";
import { useNavigate, useParams } from "react-router";

function StoryDetails() {
  const [epics, setEpics] = useState([]);
  const [priorities, setPriorities] = useState([]);
  const [storyStatuses, setStoryStatuses] = useState([]);
  const [storyData, setStoryData] = useState({});
  const form = useRef();
  const navigate = useNavigate();
  const [isReadOnly, setIsReadOnly] = useState(true);
  const { id } = useParams();
  const [serviceLineList, setServiceLineList] = useState([]);
  const [serviceLine, setServiceLine] = useState(null);
  const [associateIds, setAssociateIds] = useState([]);

  const handleChange = (e) => {
    e.preventDefault();
    const { name, value } = e.target;
    setStoryData({ ...storyData, [name]: value });
  };

  const handleDelete = (e) => {
    e.preventDefault();
    apiDeleteStory(id).then((response) => {
      alert(response.message);
      navigate("/ui/gencTracker/stories/allStories");
    });
  };

  useEffect(() => {
    apiGetAllEpics().then((response) => {
      console.log(response);
      setEpics(
        response.map((epic) => {
          return { label: epic.name, value: epic.id };
        })
      );
    });

    apiGetAllStoryStatus().then((response) => {
      console.log(response);
      setStoryStatuses(
        response.map((status) => {
          return { label: status, value: status };
        })
      );
    });

    apiGetAllPriorities().then((response) => {
      console.log(response);
      setPriorities(
        response.map((priority) => {
          return { label: priority, value: priority };
        })
      );
    });
    apiGetStoryById(id).then((response) => {
      console.log(response);
      setStoryData(response);
    });

    apiGetServiceLineList().then((response) => {
      let temp = [];
      response.map((line) => {
        temp.push({ value: line, label: line });
      });
      setServiceLineList(temp);
    });
  }, []);

  useEffect(() => {
    apiGetAssignUsersFromServiceLine(serviceLine).then((response) => {
      console.log(response);
      setAssociateIds(response);
    });
  }, [serviceLine]);

  const handleUpdate = (e) => {
    e.preventDefault();
    form.current.validateAll();
    console.log(storyData);
    apiUpdateStory(storyData)
      .then((response) => {
        alert(response.message);
        navigate("/ui/gencTracker/stories/allStories");
      })
      .catch((err) => alert(err.data.message));
  };

  return (
    <div>
      <div className="card card-container-form">
        <div
          style={{
            display: "flex",
            gap: "10px",
            justifyContent: "center",
            marginBottom: "20px",
          }}
        >
          <button
            className="btn btn-primary"
            onClick={() => setIsReadOnly(!isReadOnly)}
          >
            {isReadOnly ? "Edit" : "Editing"}
          </button>
          <button className="btn btn-danger" onClick={handleDelete}>
            Delete
          </button>
        </div>
        <label className="formheading">Story Details</label>
        <Form onSubmit={handleUpdate} ref={form}>
          {
            <div>
              <table className="tableform">
                <tbody>
                  <tr>
                    <td className="tableformlabels">
                      <label htmlFor="associateId">Subject</label>
                    </td>
                    <td>
                      <Input
                        type="text"
                        className="form-control"
                        name="subject"
                        value={storyData.subject}
                        onChange={handleChange}
                        validations={[required]}
                        disabled={isReadOnly}
                      />
                    </td>
                  </tr>
                  <tr>
                    <td className="tableformlabels">
                      <label htmlFor="fullName">Details</label>
                    </td>
                    <td>
                      <textarea
                        type="text"
                        disabled={isReadOnly}
                        className="form-control"
                        name="details"
                        value={storyData.details}
                        onChange={handleChange}
                        validations={[required]}
                        rows="9"
                      ></textarea>
                    </td>
                  </tr>
                  <tr>
                    <td className="tableformlabels">
                      <label htmlFor="skillId">Priority</label>
                    </td>
                    <td>
                      {storyData.priority != undefined && (
                        <Select
                          isDisabled={isReadOnly}
                          isClearable="false"
                          defaultValue={{
                            label: storyData.priority,
                            value: storyData.priority,
                          }}
                          onChange={(e) => {
                            setStoryData({ ...storyData, priority: e.value });
                          }}
                          options={priorities}
                        ></Select>
                      )}
                    </td>
                  </tr>
                  <tr>
                    <td className="tableformlabels">
                      <label htmlFor="skillId">Epic</label>
                    </td>
                    <td>
                      {storyData.epicId != undefined && (
                        <Select
                          onChange={(e) => {
                            setStoryData({ ...storyData, epicId: e.value });
                          }}
                          isDisabled={isReadOnly}
                          defaultValue={{
                            value: storyData.epicId,
                            label: epics.filter(
                              (epic) => epic.value == storyData.epicId
                            )[0].label,
                          }}
                          isClearable={false}
                          options={epics}
                        ></Select>
                      )}
                    </td>
                  </tr>
                  <tr>
                    <td className="tableformlabels">
                      <label htmlFor="skillId">Current Status</label>
                    </td>
                    <td>
                      {storyData.currentStatus != undefined && (
                        <Select
                          onChange={(e) => {
                            setStoryData({
                              ...storyData,
                              currentStatus: e.value,
                            });
                          }}
                          isDisabled={isReadOnly}
                          defaultValue={{
                            label: storyData.currentStatus,
                            value: storyData.currentStatus,
                          }}
                          isClearable={false}
                          options={storyStatuses}
                        ></Select>
                      )}
                    </td>
                  </tr>
                  {!isReadOnly && (
                    <tr>
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
                    </tr>
                  )}
                  <tr>
                    <td className="tableformlabels">
                      <label htmlFor="phone">Responsible </label>
                    </td>
                    <td>
                      {serviceLine == null && (
                        <Input
                          type="text"
                          className="form-control"
                          name="ownerId"
                          disabled={isReadOnly}
                          value={storyData.ownerId}
                          onChange={handleChange}
                          validations={[required]}
                        />
                      )}
                      {serviceLine != null && associateIds != [] && (
                        <Select
                          isDisabled={isReadOnly}
                          onChange={(e) =>
                            setStoryData({ ...storyData, ownerId: e.value })
                          }
                          options={associateIds}
                          validations={[required]}
                        ></Select>
                      )}
                    </td>
                  </tr>
                  <tr>
                    <td className="tableformlabels">
                      <label htmlFor="phone">Created By </label>
                    </td>
                    <td>
                      <Input
                        type="text"
                        disabled={isReadOnly}
                        className="form-control"
                        name="ownerId"
                        value={storyData.creatorId}
                        onChange={handleChange}
                        validations={[required]}
                      />
                    </td>
                  </tr>
                  <tr>
                    <td className="tableformlabels">
                      <label htmlFor="phone">Story Point Estimation</label>
                    </td>
                    <td>
                      <Input
                        type="text"
                        disabled={isReadOnly}
                        className="form-control"
                        name="storyPointEstimation"
                        value={storyData.storyPointEstimation}
                        onChange={handleChange}
                        // validations={[required]}
                      />
                    </td>
                  </tr>
                  <tr>
                    <td className="tableformlabels">
                      <label htmlFor="city">Acceptance Criteria</label>{" "}
                    </td>
                    <td>
                      <textarea
                        type="text"
                        disabled={isReadOnly}
                        className="form-control"
                        name="acceptanceStatus"
                        value={storyData.acceptanceStatus}
                        onChange={handleChange}
                        validations={[required]}
                        rows="9"
                      ></textarea>
                    </td>
                  </tr>
                  <tr>
                    <td className="tableformlabels"></td>
                    <td>
                      {!isReadOnly && (
                        <Button
                          className="btn btn-primary btn-block formsubmitbutton"
                          type="submit"
                        >
                          Update
                        </Button>
                      )}
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          }
        </Form>
      </div>
    </div>
  );
}

export default StoryDetails;
