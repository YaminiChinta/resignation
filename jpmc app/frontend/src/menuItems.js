import {
  ROLE_ASSOCIATE,
  ROLE_PM,
  ROLE_ACCOUNTLEAD,
  ROLE_LOBLEAD,
  ROLE_EDL,
  ROLE_ADMIN,
  ROLE_HR,
  ROLE_TAG,
} from "./common/constants";

export const menuData = [
  {
    name: "Home",
    needLogin: true,
    url: "/ui/home",
  },
  {
    name: "Reports ↡",
    needLogin: true,
    children: [
      {
        name: "Pyramid ↠",
        role: ROLE_PM,
        children: [
          {
            name: "Overall",
            url: "/ui/home",
          },
          {
            name: "By Practice",
            url: "/ui/report/pyramidbypractice",
          },
          {
            name: "By LOB",
            url: "/ui/report/pyramidbylob",
          },
          {
            name: "By Projects",
            url: "/ui/report/pyramidbyprojects",
          },
        ],
      },
      {
        name: "Profiles",
        role: ROLE_ASSOCIATE,
        url: "/ui/data/getAllProfiles",
      },
      {
        name: "Referrals Report",
        role: ROLE_ASSOCIATE,
        url: "/ui/data/getAllReferrals",
      },
      {
        name: "Search Associate",
        role: ROLE_PM,
        url: "/ui/data/searchAssociate",
      },
      {
        name: "Lob dashboard",
        role: ROLE_LOBLEAD,
        url: "/ui/data/LOBdashboard",
      },
      {
        name: "Edl Dashboard",
        role: ROLE_ADMIN,
        url: "/ui/data/Edldashboard",
      },
      {
        name: "Billability Report",
        role: ROLE_LOBLEAD,
        url: "/ui/report/billabilityreport",
      },
      {
        name: "Drive ↠",
        needLogin: true,
        children: [
          {
            name: "My Drive List",
            role: ROLE_ASSOCIATE,
            url: "/ui/reports/interview/drives",
          },
          {
            name: "Assigned Profiles",
            role: ROLE_ASSOCIATE,
            url: "/ui/reports/interview/assignedprofiles",
          },
          {
            name: "Drive Nominations",
            role: ROLE_TAG,
            url: "/ui/reports/interview/alldrives",
          },
          {
            name: "Associate Participations",
            role: ROLE_LOBLEAD,
            url: "/ui/drive/allAssociates",
          },
          {
            name: "Panel Availabilities",
            role: ROLE_TAG,
            url: "/ui/drive/calendarView",
          },
        ],
      },
      {
        name: "Leaves Report",
        needLogin: true,
        role: ROLE_PM,
        url: "ui/leaves/adminPanel",
      },
    ],
  },
  {
    name: "Forms ↡",
    needLogin: true,
    children: [
      {
        name: "Upload Assignment",
        role: ROLE_LOBLEAD,
        url: "/ui/forms/uploadAssignment",
      },
      {
        name: "Profiles ↠",
        role: ROLE_ASSOCIATE,
        children: [
          {
            name: "Internal Profile",
            url: "/ui/profiles/internalprofile",
          },
          {
            name: "External Profile",
            url: "/ui/profiles/externalprofile",
          },
        ],
      },
      {
        name: "Referrals ↠",
        role: ROLE_ASSOCIATE,
        url: "ui/referrals",
      },
      {
        name: "Billability ↠",
        role: ROLE_LOBLEAD,
        children: [
          {
            name: "Billable Plans",
            url: "/ui/forms/billability/billableplans",
          },
        ],
      },
      {
        name: "Interview Drives ↠",
        children: [
          {
            name: "Create Drive",
            role: ROLE_TAG,
            url: "/ui/forms/interview/newdrive",
          },
          {
            name: "Add My Availability",
            role: ROLE_ASSOCIATE,
            url: "/ui/forms/interview/registerpanelist",
          },
        ],
      },
      {
        name: "Leaves ↠",
        children: [
          {
            name: "My Leaves",
            needLogin: true,
            role: ROLE_ASSOCIATE,
            url: "/ui/leaves/userPanel",
          },
          {
            name: "My Associate Group",
            needLogin: true,
            role: ROLE_PM,
            url: "/ui/groupPanel",
          },
        ],
      },

      {
        name: "Trainings ↠",
        role: ROLE_ASSOCIATE,
        children: [
          {
            name: "Add Training",
            role: ROLE_HR,
            url: "/ui/forms/trainings/add",
          },
          {
            name: "List of Trainings",
            role: ROLE_ASSOCIATE,
            url: "/ui/forms/trainings",
          },
          {
            name: "Manager view",
            role: ROLE_HR,
            url: "/ui/forms/trainings?progress=avg",
          },
        ],
      },
    ],
  },
  {
    name: "GenC Tracker ↡",
    needLogin: true,
    role: ROLE_ASSOCIATE,
    children: [
      {
        name: "Dashboard",
        url: "/ui/gencTracker/dashboard",
        role: ROLE_PM,
      },
      {
        name: "All Stories",
        url: "/ui/gencTracker/stories/allStories",
        role: ROLE_PM,
      },
      {
        name: "New Story",
        url: "/ui/gencTracker/stories/newStory",
        role: ROLE_PM,
      },
      {
        name: "All Sprints",
        url: "/ui/gencTracker/sprint/allSprints",
        role: ROLE_PM,
      },
      {
        name: "New Sprint",
        url: "/ui/gencTracker/sprint/newSprint",
        role: ROLE_ADMIN,
      },
      {
        name: "Create Epic",
        url: "/ui/gencTracker/addepic",
        role: ROLE_PM,
      },
      {
        name: "All Epics",
        url: "/ui/gencTracker/epics",
        role: ROLE_PM,
      },
      {
        name: "Parent Accounts",
        url: "/ui/gencTracker/parent-accounts",
        role: ROLE_PM,
      },
      {
        name: "PDLs",
        url: "/ui/gencTracker/pdls",
        role: ROLE_PM,
      },
      {
        name: "SBUs",
        url: "/ui/gencTracker/sbus",
        role: ROLE_PM,
      },
    ],
  },
  {
    name: "Admin ↡",
    needLogin: true,
    role: ROLE_ADMIN,
    children: [
      {
        name: "Settings",
        url: "/ui/admin/settings",
      },
      {
        name: "Approve Registrations",
        url: "/ui/Admin/approval",
      },
    ],
  },
  {
    name: "Login",
    needLogin: false,
    url: "/ui/login",
  },
  {
    name: "Register",
    needLogin: false,
    url: "/ui/register",
  },
  {
    name: "User ↡",
    needLogin: true,
    children: [
      {
        name: "Change Password",
        needLogin: true,
        role: ROLE_ASSOCIATE,
        url: "/ui/change-password",
      },
      {
        name: "Logout",
        needLogin: true,
        url: "/ui/logout",
      },
      {
        name: "Templates ↠",
        role: ROLE_ASSOCIATE,
        children: [
          {
            name: "Assignment Listing",
            role: ROLE_ASSOCIATE,
            url: "/ui/dummy/assignmentlisting",
          },
          {
            name: "Form",
            role: ROLE_ASSOCIATE,
            url: "",
          },
          {
            name: "List and Form",
            role: ROLE_ASSOCIATE,
            url: "",
          },
        ],
      },
    ],
  },
];
