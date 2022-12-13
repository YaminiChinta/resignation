import { useEffect, useState } from "react";
import "./Calendar.css";
import InterviewDriveService from "../../services/InterViewDrive.service";
import { Link } from "react-router-dom";
import { UI_URL } from "../../common/constants";

const Calendar = () => {
    const todayDate = new Date();
    const [dateMapp, setDateMapp] = useState(new Map());
    const dateMap = new Map();

    useEffect(() => {
        InterviewDriveService.getCalenderViewForDrivePanelist("ADM")
            .then(data => {
                console.log(data);
                for (var i in data) {
                    const calDate = formatADate(new Date(i));
                    dateMap.set(calDate, data[i]);
                }
                setDateMapp(dateMap);

            })
    }, [])

    const daysInMonth = (month, year) => {
        return new Date(year, month, 0).getDate();
    }

    const formatADate = (date) => {
        const formatDate =
            date.getDate() < 10 ? `0${date.getDate()}` : date.getDate();
        const formatMonth =
            date.getMonth() < 9
                ? `0${date.getMonth() + 1}`
                : date.getMonth() + 1;
        const formatteddate = [date.getFullYear(), formatMonth, formatDate].join("-");
        return formatteddate;
    }
    const printMonth = (vDate) => {

        let month = vDate.getMonth();
        let noOfDays = daysInMonth(month, vDate.getFullYear());
        let rows = [];
        let tempDate = new Date(vDate.getFullYear(), month, 1);
        let firstDay = tempDate.getDay();
        for (let i = 1; i <= firstDay; i++) {
            rows.push(<li></li>);
        }
        for (let i = 1; i <= noOfDays; i++) {

            let tempdate = new Date(vDate.getFullYear(), month, i);
            const calDate = formatADate(tempdate);

            rows.push(
                <li className="daycss">
                    {i}
                    <br />
                    <label className="panelistcount">
                        <Link
                            to={UI_URL + `drive/calendarView/panelist`}
                            state={{ calDate: calDate }}
                        >
                            {dateMapp.get(calDate) ? dateMapp.get(calDate) : ""}
                            {/* {dateMapp.get(calDate) ? dateMapp.get(calDate).panelsAvailable + "/" + dateMapp.get(calDate).totalPanels : ""} */}
                        </Link>
                    </label>
                </li >
            );
        }
        // for (let i = 0; i < (38 - (firstDay + noOfDays)); i++) {
        //     rows.push(<li>1</li>);
        // }
        return <tbody className="tbodyCal">{rows}</tbody>;
    }

    const renderMonth = (vDate) => {
        return (
            <table key={vDate} >
                <tr>
                    <td className="monthcalendar">
                        <div className="month">
                            <ul>
                                <li>
                                    {vDate.toLocaleString('default', { month: 'long' })}
                                    &nbsp;
                                    <span style={{ "font-size": "20px" }}>{vDate.getFullYear()}</span>
                                </li>
                            </ul>
                        </div>
                        <ul className="weekdays">
                            <li>Su</li>
                            <li>Mo</li>
                            <li>Tu</li>
                            <li>We</li>
                            <li>Th</li>
                            <li>Fr</li>
                            <li>Sa</li>
                        </ul>
                        <ul className="days">
                            {printMonth(vDate)}
                        </ul>
                    </td>

                </tr>
            </table>
        )
    }

    const renderYear = (vDate) => {
        const Q1 = [0, 1, 2];
        const Q2 = [3, 4, 5];
        const Q3 = [6, 7, 8];
        const Q4 = [9, 10, 11];
        return (<>
            <table>
                <tr>
                    {
                        Q1.map(day => (
                            <td >
                                {renderMonth(new Date(vDate.getFullYear(), day, 1))}
                            </td>
                        ))
                    }
                </tr>
                <tr>
                    {
                        Q2.map(day => (
                            <td >
                                {renderMonth(new Date(vDate.getFullYear(), day, 1))}
                            </td>
                        ))
                    }
                </tr>
                <tr>
                    {
                        Q3.map(day => (
                            <td >
                                {renderMonth(new Date(vDate.getFullYear(), day, 1))}
                            </td>
                        ))
                    }
                </tr>
                <tr>
                    {
                        Q4.map(day => (
                            <td >
                                {renderMonth(new Date(vDate.getFullYear(), day, 1))}
                            </td>
                        ))
                    }
                </tr>
            </table>
        </>)

    }

    return (
        <>
            {/* <h1> Calendar</h1> */}
            {renderYear(todayDate)}

        </>);
};

export default Calendar;
