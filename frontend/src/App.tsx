import React from "react";
import { useState } from "react";
import { CopyToClipboard } from "react-copy-to-clipboard";
import "./App.css";

import { IoCheckmark, IoWarning } from "react-icons/io5";
import { SafetyStatus, UrlSafetyReport } from "./model/UrlSafetyReport";
import { VscWorkspaceUnknown } from "react-icons/vsc";

function mapSafetyStatusToComponent(safetyStatus: string) {
  const COMPONENT_SAFETY_STATUS_MAP = {
    YES: <IoCheckmark />,
    NO: <IoWarning />,
    UNKNOWN: <VscWorkspaceUnknown />,
  };

  return COMPONENT_SAFETY_STATUS_MAP[safetyStatus];
}

function ShowSafetyParamsPopup(props) {
  const safetyParams = props.safetyParams;

  return (
    <div style={{ textAlign: "left" }}>
      <div>
        Syntax is correct:
        {mapSafetyStatusToComponent(safetyParams.syntaxIsCorrect)}
      </div>
      <div>
        Site is alive:
        {mapSafetyStatusToComponent(safetyParams.siteIsAlive)}
      </div>
    </div>
  );
}

function App() {
  const [url, setUrl] = useState<any | null>("");
  const [shortenedUrl, setShortenedUrl] = useState("");
  const [safetyReport, setSafetyReport] = useState<UrlSafetyReport>({
    status: SafetyStatus.UNKNOWN,
    safetyParams: {
      siteIsAlive: SafetyStatus.UNKNOWN,
      syntaxIsCorrect: SafetyStatus.UNKNOWN,
    },
  });

  const [showPopup, setShowPopup] = useState(false);

  const API_URL = "http://localhost:8081/api/v1/url-shortener";

  const shortenUrl = async (e) => {
    e.preventDefault();

    try {
      const payload = {
        url: url,
      };

      const config = {
        method: "POST",
        headers: {
          Accept: "application/json",
          "Content-Type": "application/json",
        },
        body: JSON.stringify(payload),
      };

      const response = await fetch(API_URL, config);
      const data = await response.json();

      setShortenedUrl(data.url);
      setSafetyReport(data.urlSafetyReport);
    } catch (e) {
      alert(e);
    }
  };

  return (
    <div className="app">
      <div className="shortener">
        <h2>URL shortener</h2>
        {/* form to enter URL to be shortened */}
        <form onSubmit={shortenUrl}>
          <input
            id="url-bar"
            placeholder="Enter URL"
            value={url}
            onChange={(e) => setUrl(e.target.value)}
          />
          <button id="shorten-url-button">Submit</button>
        </form>
        {/* Section to view shortened URLS */}
        {shortenedUrl && (
          <div id="shortened-url" className="shortener__viewShot">
            {shortenedUrl}
            <CopyToClipboard text={shortenedUrl}>
              <button onClick={() => alert("The URL has been copied")}>
                copy
              </button>
            </CopyToClipboard>
            <button onClick={() => setShowPopup(!showPopup)}>
              {mapSafetyStatusToComponent(safetyReport.status)}
            </button>
            {showPopup && (
              <ShowSafetyParamsPopup safetyParams={safetyReport.safetyParams} />
            )}
          </div>
        )}
      </div>
    </div>
  );
}

export default App;
