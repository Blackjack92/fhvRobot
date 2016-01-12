﻿using System;
using System.Windows.Input;
using GameServer.Controllers;
using GameServer.Managers;
using GameServer.Models;
using GameServer.Services;
using GameServer.Utils;
using PostSharp.Patterns.Model;
using OpenFileDialog = Microsoft.Win32.OpenFileDialog;
using SaveFileDialog = Microsoft.Win32.SaveFileDialog;

namespace GameServer.ViewModels
{
    [NotifyPropertyChanged]
    public class MainViewModel
    {
        #region Properties
        public Score CurrentScore { get; private set; }
        public ScoreManager ScoreManager { get; private set; }
        public ICommand SendMessageCommand { get; private set; }
        public ICommand TestCommand { get; private set; }
        public ICommand OpenCommand { get; private set; }
        public ICommand SaveCommand { get; private set; }
        public TimerService TimerService { get; private set; }
        #endregion

        #region Fields
        private readonly NetworkServer server;
        private readonly PersistencyManager persistencyManager;
        #endregion

        #region ctor
        public MainViewModel()
        {
            ScoreManager = new ScoreManager();
            persistencyManager = new PersistencyManager();

            // TODO: Discuss if the toggle start stop should be refactored
            TimerService = new TimerService();
            TimerService.ToggleStartStop();
            
            SendMessageCommand = new DelegateCommand(SendMessage);
            TestCommand = new DelegateCommand(o => SetTestData());
            OpenCommand = new DelegateCommand(o => LoadScore());
            SaveCommand = new DelegateCommand(o => SaveScore());

            server = new NetworkServer();
            //server.Start();
        }
        #endregion

        #region Methods
        private void SetTestData()
        {
            CurrentScore = new Score { Name = "MyPlayer", Duration = new TimeSpan() };

            ScoreManager.Clear();
            ScoreManager.Add(new Score { Name = "Peter", Duration = new TimeSpan() });
            ScoreManager.Add(new Score { Name = "Klaus", Duration = new TimeSpan() });
            ScoreManager.Add(new Score { Name = "Johannes", Duration = new TimeSpan() });
            ScoreManager.Add(new Score { Name = "Bernhard", Duration = new TimeSpan() });
            ScoreManager.Add(new Score { Name = "Mathias", Duration = new TimeSpan() });
            ScoreManager.Add(new Score { Name = "Julia", Duration = new TimeSpan() });
            ScoreManager.Add(new Score { Name = "Max", Duration = new TimeSpan() });
            ScoreManager.Add(new Score { Name = "Johanna", Duration = new TimeSpan() });
            ScoreManager.Add(new Score { Name = "Mario", Duration = new TimeSpan() });
        }

        private void SendMessage(object obj)
        {
            server.SendMessage(CurrentScore.Name);
            TimerService.ToggleStartStop();
        }

        private void SaveScore()
        {
            var saveFileDialog = new SaveFileDialog
            {
                Filter = "XML Files (*.xml)|*.xml",
                Title = "Save as Xml File"
            };

            saveFileDialog.ShowDialog();

            // If the file name is not an empty string open it for saving.
            var fileName = saveFileDialog.FileName;
            if (fileName != "")
            {
                persistencyManager.SaveScores(ScoreManager.GetAllScores(), fileName);
            }
        }

        private void LoadScore()
        {
            var openFileDialog = new OpenFileDialog
            {
                InitialDirectory = "c:\\",
                Filter = "XML Files (*.xml)|*.xml",
                Title = "Load from Xml File",
                RestoreDirectory = true
            };

            openFileDialog.ShowDialog();

            var fileName = openFileDialog.FileName;
            if (fileName != "")
            {
                ScoreManager.SetAllScores(persistencyManager.LoadScores(fileName));
            }
        }

        #endregion
    }
}
